package grails.melody.plugin

import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import net.bull.javamelody.JdbcWrapper
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@Integration
class GrailsTransactionSpec extends Specification {

    @Transactional
    @Rollback
    def 'saving an domain instance should not throw IllegalStateException - validate connection wrapper'() {
        when:
        new SampleDomain().save(flush:true)

        then:
        notThrown IllegalStateException
        SampleDomain.count() == 1

        def requests = JdbcWrapper.SINGLETON.sqlCounter.requests
        requests.size() == 2
        requests[0].name.startsWith('insert into sample_domain')
        requests[1].name.startsWith('select count') //Domain class afterInsert method
    }

}