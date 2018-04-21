# ∫ Integral Canary

A simple [miner's canary](https://en.wikipedia.org/wiki/Domestic_canary#Miner's_canary) to get some additional
logging/system status without a big monitoring setup.

## Why?

We wanted an easy way to monitor some key aspects (MySQL connection pool, Mongo Health etc.) without another big framework
or logging/monitoring solution.

## Usage

You have to provide a configuration, the checks (see `example` folder) and call the service.

Include the `CanaryModule.class` in your apps module:

```java
@ImportModule({ CanaryModule.class })
public class AppModule {
    ...
}
```

### `CanaryConfig`

There is no default config bound to the `ServiceBinder` so you have to actively provide one. The easiest way is a simple
builder method:

```java
@ImportModule({ CanaryModule.class })
public class AppModule {

    public static CanaryConfig buildCanaryConfig() {
        return CanaryConfigBuilder.defaultValues();
    }
}
```


### `CanaryCheck`

See `example` folder for how to implement a `CanaryCheck`.

```java
@ImportModule({ CanaryModule.class })
public class AppModule {

    @Contribute(CanaryService.class)
    public static void contributeCanaryService(OrderedConfiguration<CanaryCheck> conf) {
        conf.addInstance("check-1", MyFirstCheck.class);
        conf.addInstance("check-0", RunBeforeAllCheck.class, "before:*");
    }
}
```


### Running the Checks

The checks aren't run magically in the background, you have to do that yourself. E.g. we use our `HAProxy` status request
with a `HttpServletRequestFilter`, but you could also use an ordinary scheduler or something.

Another simple way would be using the Tapestry Scheduler:

```java
@ImportModule({ CanaryModule.class })
public class AppModule {

    @Startup
    public static void scheduleCanaryCheck(CanaryService canaryService, PeriodicExecutor executor) {
        executor.addJob(new IntervalSchedule(Duration.ofMinutes(1).toMillis()), "canary-check", canaryService::check);
    }
}
```


### Symbols
There's only one symbol available so far: `net.netzgut.integral.canary.enabled` with default `true`.  
If you disabled the canary a log message (level info) will be printed by `CanaryService` on build, and every
`CanaryService#check()` will return an `CanarySystemState` with `Ok`.


## Contribute

It's awesome that you want to contribute! Please see [this repository](https://github.com/netzgut/contribute)
for more details.


## License

Apache 2.0 license, see [LICENSE.txt](LICENSE.txt].
