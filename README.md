# Spring boot RabbitMQ example

To test it:

Clone the project

`cd  PROJECT_FOLDER`

Start rabbitMQ: `docker-compose up`

Import the project as Gradle Project in you IDE

Start the `Application class` as Java Application

Open the RabbitMq management Interface at: http://localhost:15672  (user: admin, password: admin)

Select `exchanges` --> `mydomain.KPIS`

Expand `Publish message`

Insert as routing key: `/`

Insert as payload: 

```
{
  "windowId":"1522751098000-1522752198000",
  "kpis":["1","2","3"]
}
```

Click on `Publish message`

In the console of you IDE you can see the following logs:

```
2018-10-17 16:10:46.436  INFO 4416 --- [containerKpis-1] Application                              : ---> kpisEvent RECEIVED
2018-10-17 16:10:46.442  INFO 4416 --- [containerKpis-1] Application                              : ---> kpisEvent DECODED, windowId: 1522751098000-1522752198000
```

Select `exchanges` --> `myevent.ALERT`

Expand `Publish message`

Insert as routing key: `/`

Insert as payload: 

```
{
  "timestamp":1522751098000,
  "code": "USER_LOGGED_IN",
  "severity": "AUDIT",
  "message": "user1"
}
```

Click on `Publish message`

In the console of you IDE you can see the following logs:

```
2018-10-17 16:11:31.636  INFO 4416 --- [ontainerReset-1] Application                              : ---> kpisAlert RECEIVED
2018-10-17 16:11:31.638 ERROR 4416 --- [ontainerReset-1] Application                              : ---> Message: user1
```

Until now everything worked as expected. Now I would like to publish an Alert event (check the class `BrokerExceptionHandler`) if an exception occurs in the `Application.kpisEvent` method.

To test it:


Open the RabbitMq management Interface at: http://localhost:15672

Select `exchanges` --> `mydomain.KPIS`

Expand `Publish message`

Insert as routing key: `/`

Insert as payload: 

```
WRONG JSON FORMAT
```

Click on `Publish message`

In the console of you IDE you can see the following logs:

```
2018-10-17 16:13:41.926  INFO 4416 --- [containerKpis-1] Application                              : ---> kpisEvent RECEIVED
2018-10-17 16:13:41.926 ERROR 4416 --- [containerKpis-1] BrokerExceptionHandler                   : Exception Detected. Publishing error alert
---> Before publishing Alert event
2018-10-17 16:13:41.931  INFO 4416 --- [containerKpis-1] o.s.a.r.l.SimpleMessageListenerContainer : Restarting Consumer@328d044f: tags=[{amq.ctag-RW0c7mfZD5m7V_tBmD6h_w=myapp.mydomain.KPIS}], channel=Cached Rabbit Channel: AMQChannel(amqp://admin@127.0.0.1:5672/,1), conn: Proxy@717fba3f Shared Rabbit Connection: SimpleConnection@4c657904 [delegate=amqp://admin@127.0.0.1:5672/, localPort= 55877], acknowledgeMode=AUTO local queue size=0
2018-10-17 16:13:41.948  INFO 4416 --- [containerKpis-2] o.s.amqp.rabbit.core.RabbitAdmin         : Auto-declaring a non-durable, auto-delete, or exclusive Queue (myapp.mydomain.KPIS) durable:false, auto-delete:true, exclusive:true. It will be redeclared if the broker stops and is restarted while the connection factory is alive, but all messages will be lost.
2018-10-17 16:13:41.948  INFO 4416 --- [containerKpis-2] o.s.amqp.rabbit.core.RabbitAdmin         : Auto-declaring a non-durable, auto-delete, or exclusive Queue (myapp.myevent.ALERT) durable:false, auto-delete:true, exclusive:true. It will be redeclared if the broker stops and is restarted while the connection factory is alive, but all messages will be lost.
```

As you can see in the above logs the Alert event has not been published.



