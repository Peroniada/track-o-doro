package com.sperek.application.api.query


import spock.lang.Specification

import java.time.LocalDate
import java.time.ZoneId

import static com.sperek.trackodoro.tracker.session.PomodoroSessionBuilder.aPomodoroSession

class QueryResolverTest extends Specification {

    QueryResolver queryResolver

    def setup() {
        setup:
        queryResolver = new QueryResolver();
    }

    def "Should resolve a query with category and date"() {
        given: "A query with category and date"
        def ownerId = UUID.fromString("fe1e0909-c5bf-483f-a6ed-96abf85f58c4")
        def query = [
                date    : ["2019-07-24"] as List<String>,
                category: ["Books"] as List<String>
        ]

        def satisfyingSession = aPomodoroSession()
                .withActivityName("")
                .withDuration(25)
                .withId(UUID.randomUUID())
                .withOwnerId(ownerId)
                .withCategory("Books")
                .withOccurrence(LocalDate.parse("2019-07-24").atStartOfDay(ZoneId.systemDefault()))
                .build()

        def notSatisfyingSession = aPomodoroSession()
                .withActivityName("")
                .withDuration(25)
                .withId(UUID.randomUUID())
                .withOwnerId(ownerId)
                .withCategory("Coding")
                .withOccurrence(LocalDate.parse("2019-07-28").atStartOfDay(ZoneId.systemDefault()))
                .build()

        when: "Query is resolved"
        def specificationResult = queryResolver.resolve(ownerId, query)

        then: "Query matches expected specification"
        specificationResult.isSatisfiedBy(satisfyingSession)
        !(specificationResult.isSatisfiedBy(notSatisfyingSession))
    }
}
