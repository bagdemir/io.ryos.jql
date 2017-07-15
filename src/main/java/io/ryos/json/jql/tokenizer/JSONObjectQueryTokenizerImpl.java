package io.ryos.json.jql.tokenizer;

import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the {@link JSONObjectQueryTokenizer}. It is basically a state machine to
 * tokenize the query into tokens which will be transformed into {@link Selector} instances.
 * @author Bagdemir
 * @version 1.0
 * @since 1.0
 * @see {@link Selector}
 */
public class JSONObjectQueryTokenizerImpl implements JSONObjectQueryTokenizer {

    private final LinkedList<Selector> selectors = new LinkedList<>();
    private State state = State.NOT_STARTED;
    private StringBuffer buffer = new StringBuffer();

    @Override
    public List<Selector> read(String query) {

        if (query != null && !query.isEmpty()) {
            char[] queryChars = query.toCharArray();
            for (final char current : queryChars) {
                switch (current) {
                    case '.':
                        handleDot();
                        break;
                    case '[':
                        handleStartArray();
                        break;
                    case ']':
                        handleEndArray();
                        break;
                    case '"':
                        handleDoubleQuote();
                        break;
                    default:
                        handleOtherwise(current);
                }
            }
            handleEnd();
            state = State.END;
        }
        return selectors;
    }

    private void handleOtherwise(char c) {
        if (state == State.OBJ_SELECTION_STARTED) {
            state = State.OBJ_SELECTION_READING;
        } else if (state != State.OBJ_SELECTION_READING) throw new UnexpectedTokenException();
        buffer.append(c);
    }

    private void handleDoubleQuote() {
        switch (state) {
            case OBJ_SELECTION_STARTED:
                state = State.OBJ_SELECTION_STARTED_E;
                break;
            case OBJ_SELECTION_READING_E:
                state = State.OBJ_SELECTION_ENDED_E;
                break;
            default:
                throw new UnexpectedTokenException();
        }
    }

    private void handleEndArray() {
        switch (state) {
            case ARR_SELECTION_STARTED:
            case ARR_SELECTION_READING:
                state = State.ARR_SELECTION_ENDED;
                selectors.add(new ArraySelectorImpl<>(buffer.toString()));
                flushBuffer();
                break;
            case OBJ_SELECTION_READING_E:
            case OBJ_SELECTION_STARTED_E:
                break;
            default:
                throw new UnexpectedTokenException();
        }
    }

    private void handleStartArray() {
        switch (state) {
            case OBJ_SELECTION_STARTED:
            case OBJ_SELECTION_READING:
                state = State.ARR_SELECTION_STARTED;
                break;
            case OBJ_SELECTION_STARTED_E:
                state = State.OBJ_SELECTION_READING_E;
                break;
            case OBJ_SELECTION_READING_E:
                break;
            default:
                throw new UnexpectedTokenException();
        }
    }

    private void handleDot() {
        switch (state) {
            case NOT_STARTED:
                state = State.OBJ_SELECTION_STARTED;
                selectors.add(new SelfSelectorImpl<>());
                break;
            case OBJ_SELECTION_READING:
            case OBJ_SELECTION_ENDED_E:
                state = State.OBJ_SELECTION_STARTED;
                selectors.add(new ObjectSelectorImpl<>(buffer.toString()));
                selectors.add(new SelfSelectorImpl<>());
                flushBuffer();
                break;
            case OBJ_SELECTION_READING_E:
                break;
            default:
                throw new UnexpectedTokenException();
        }
    }

    private void handleEnd() {
        switch (state) {
            case OBJ_SELECTION_STARTED:
                break;
            case OBJ_SELECTION_READING:
            case OBJ_SELECTION_ENDED_E:
                selectors.add(new ObjectSelectorImpl<>(buffer.toString()));
                break;
            case ARR_SELECTION_STARTED:
            case ARR_SELECTION_READING:
                selectors.add(new ArraySelectorImpl<>(buffer.toString()));
                break;
            case OBJ_SELECTION_READING_E:
                break;
            default:
                throw new UnexpectedTokenException();
        }
    }

    private void flushBuffer() {
        buffer = new StringBuffer();
    }

    private enum State {
        NOT_STARTED,
        OBJ_SELECTION_STARTED,
        OBJ_SELECTION_READING,
        OBJ_SELECTION_STARTED_E,
        OBJ_SELECTION_READING_E,
        OBJ_SELECTION_ENDED_E,
        END,
        ARR_SELECTION_STARTED,
        ARR_SELECTION_READING,
        ARR_SELECTION_ENDED
    }

    private class UnexpectedTokenException extends RuntimeException {
    }
}
