package hello.advanced.trace.hellotrace;

import hello.advanced.trace.TraceStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HelloTraceV1Test {

    @Test
    void begin_end(){
        final HelloTraceV1 trace = new HelloTraceV1();
        final TraceStatus status = trace.begin("hello");
        trace.end(status);
    }

    @Test
    void begin_exception(){
        final HelloTraceV1 trace = new HelloTraceV1();
        final TraceStatus status = trace.begin("hello");
        trace.exception(status, new IllegalArgumentException());
    }

}