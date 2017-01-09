# Reward System

## Running the App

Execute ```./gradlew build && java -jar build/libs/gs-spring-boot-0.1.0.jar``` from within the 
app directory.

Easier would be loading this up in IntelliJ (with Java 8) as a gradle app, and just hitting run.

## Overview

I'm most comfortable with Java, but actually don't have experience standing up Java web apps from scratch
(most of my experience is from writing algo logic into existing systems, often for offline processes).
After investigating some different tools, Spring Boot seemed like the fastest path forward for a code test.
There are some shortcuts I took in testing (partially a reflection of the fact I'm new to this framework),
commented inline in the code. I did write some simple tests to cover the functionality of the api.
All told, standing this up took about 3.5 hours (from starting with researching spring boot).

I renamed ```amount``` to ```delta``` for a transfer, because it makes a bit more sense to me that way.
I'd probably also implement transfers with a time stamp, and have a cache table that updates on a regular
schedule (nightly, perhaps) for getting a user's points balance as of a certain time (instead of storing
points on the `User`). Some points balance caching strategy would be necessary to scale the system. 
As long as there aren't many point balances, the points update method implemented here suffices.

## Tests

I wrote some basic tests that partially cover the services. For the sake of time (since the prompt was
a self-described "toy app"), I didn't write a test base class that handles hibernate session stuff,
and instead just tagged the tests with @Transactional.

I also wrote some sample controller tests, which mock up the MVC and the services, and then run through
the API calls. 