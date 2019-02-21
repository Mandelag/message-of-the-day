package com.mandelag.app.motd;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.*;

public class MotdActor extends AbstractActor {
  private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
  private final Collection<String> messagesOfTheDay = new HashSet<String>();

  static final class GetMotd {}

  static final class AddMotd {
    public final String message;

    public AddMotd(String message) {
      this.message = message;
    }
  }

  public MotdActor(String ...initialMessages) {
    this.messagesOfTheDay.addAll(Arrays.asList(initialMessages));
  }

  public static Props props(String ...initialMessages) {
    return Props.create(MotdActor.class, () -> new MotdActor(initialMessages));
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(MotdActor.GetMotd.class, x -> {
          String messageOfTheDay = this.getMessageOfTheDay().orElse("Kamu luar biasa");
          getSender().tell(messageOfTheDay, getSelf());
        })
        .match(MotdActor.AddMotd.class, x -> {
          this.messagesOfTheDay.add(x.message);
          log.debug("Received new message of the day: " + x.message);
        })
        .matchAny(x -> {
          log.debug("Received unknown message!");
        })
        .build();
  }

  // https://stackoverflow.com/questions/21092086/get-random-element-from-collection
  private Optional<String> getMessageOfTheDay() {
    int num = (int) (Math.random() * this.messagesOfTheDay.size());
    for(String message: this.messagesOfTheDay) if (--num < 0) return Optional.of(message);
    return Optional.of(null);
  }
}
