package com.ge;

import java.util.function.Function;

import javax.inject.Inject;

import io.quarkus.runtime.annotations.QuarkusMain;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@QuarkusMain
public class App {
  private static String result;

  public static void main(String[] args) {
    Uni.createFrom().emitter(em -> {em.complete("heloooo");});
    Multi.createFrom().item("helloo").onItem().transform(item -> item).subscribe().with(item -> System.out.println(item));
    Uni.createFrom().item("helloo").onItem().transform(item -> item+"mutiny").subscribe().with(item -> System.out.println(item));
  }
}