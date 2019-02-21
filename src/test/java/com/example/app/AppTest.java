/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.example.app;

import akka.actor.ActorSystem;
import akka.actor.ActorRef;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.testkit.JUnitRouteTest;
import akka.http.javadsl.testkit.TestRoute;
import org.junit.Assert;
import org.junit.Test;

public class AppTest extends JUnitRouteTest {

  @Test
  public void testhello() {
    ActorSystem testSystem = ActorSystem.create("test-system");
    ActorRef handler = testSystem.actorOf(MotdActor.props("mantap!"));
    TestRoute appRoute = testRoute(App.appRoute(handler));
    appRoute.run(HttpRequest.GET("/"))
        .assertStatusCode(200)
        .assertEntity("mantap!");
//    Assert.assertEquals(5, 5);
  }
}