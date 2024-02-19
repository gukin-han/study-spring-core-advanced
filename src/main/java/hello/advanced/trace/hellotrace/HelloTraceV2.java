package hello.advanced.trace.hellotrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HelloTraceV2 {

    private static final String START_PREFIX = "-->";
    private static final String COMPlETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    public TraceStatus begin (String message) {
        final TraceId traceId = new TraceId();
        final long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    };

    // v2에서 추가
    public TraceStatus beginSync (TraceId beforeTraceId, String message) {
//        final TraceId traceId = new TraceId();
        final TraceId nextId = beforeTraceId.createNextId();
        final long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", nextId.getId(), addSpace(START_PREFIX, nextId.getLevel()), message);
        return new TraceStatus(nextId, startTimeMs, message);
    };

    public void end(TraceStatus status) {
        complete(status, null);
    };

    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        final Long stopTimeMs = System.currentTimeMillis();
        final long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        final TraceId traceId = status.getTraceId();

        if (e == null) {
            log.info("[{}] {}{} time={}ms",
                    traceId.getId(),
                    addSpace(COMPlETE_PREFIX, traceId.getLevel()),
                    status.getMessage(),
                    resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex={}",
                    traceId.getId(),
                    addSpace(COMPlETE_PREFIX, traceId.getLevel()),
                    status.getMessage(),
                    resultTimeMs,
                    e.toString());
        }
    }

    private String addSpace(String prefix, int level) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append((i == level - 1) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }
}
