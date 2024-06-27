package com.alexeyzarechnev.mafia.game;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.StringReader;
import java.io.StringWriter;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Parser tests")
public class ParserTests {

    private Parser parser;
    private StringReader reader;
    private StringWriter writer;
    private String nl = Parser.nl;

    private void init(String input) {
        reader = new StringReader(input);
        writer = new StringWriter();
        parser = new Parser(reader, writer);
    }    

    @Test
    @DisplayName("Empty test")
    public void emptyTest() {
        init("");

        @SuppressWarnings("unchecked")
        List<String>[] result = new List[1];

        assertDoesNotThrow(() -> result[0] = parser.expect(List.of(), "Enter"));
        assertEquals("", writer.toString());
        assertEquals(List.of(), result[0]);
    }

    @Test
    @DisplayName("No input test")
    public void noInputTest() throws Exception {
        init("");
        assertThrows(InvalidAlgorithmParameterException.class ,() -> parser.expect(List.of(Answer.NAME), "Enter your name"));
        assertEquals("Enter your name" + nl + "Do not close the input during the game" + nl, writer.toString());
    }

    private static Stream<Answer> answerVariants() {
        return Stream.of(
            Answer.NAME,
            Answer.KILL_COMMAND,
            Answer.CHECK_COMMAND,
            Answer.HEAL_COMMAND
        );
    }

    @ParameterizedTest
    @MethodSource("answerVariants")
    public void oneArgumentTest(Answer expect) {
        init(expect.toString());

        @SuppressWarnings("unchecked")
        List<String>[] result = new List[1];

        assertDoesNotThrow(() -> result[0] = parser.expect(List.of(expect), "Enter your " + expect));
        assertEquals("Enter your " + expect + nl, writer.toString());
        assertEquals(List.of(expect.toString()), result[0]);
    }

    @Test
    @DisplayName("Wrong input")
    public void wrongInputTest() {
        init("wrong");
        assertThrows(InvalidAlgorithmParameterException.class, () -> parser.expect(List.of(Answer.KILL_COMMAND), ""));
        assertEquals(nl + "Incorrect argument: 'wrong'" + nl + "Do not close the input during the game" + nl, writer.toString());    
    }

    private static class AnswerArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                Arguments.of(Answer.NAME, Answer.NAME),
                Arguments.of(Answer.KILL_COMMAND, Answer.NAME),
                Arguments.of(Answer.CHECK_COMMAND, Answer.NAME),
                Arguments.of(Answer.HEAL_COMMAND, Answer.NAME)
            );
        }
        
    }

    @ParameterizedTest
    @ArgumentsSource(AnswerArgumentsProvider.class)
    public void twoArgumentsTest(Answer expect1, Answer expect2) {
        init(expect1 + " " + expect2);

        @SuppressWarnings("unchecked")
        List<String>[] result = new List[1];

        assertDoesNotThrow(() -> result[0] = parser.expect(List.of(expect1, expect2), ""));
        assertEquals(List.of(expect1.toString(), expect2.toString()), result[0]);
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 4, 5, 6, 7, 8, 9, 10})
    public void manyArgumentsTest(int count) {
        List<Answer> expected = new ArrayList<Answer>();
        List<String> parsed = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            expected.add(Answer.NAME);
            parsed.add(Answer.NAME.toString());
            sb.append(Answer.NAME.toString()).append(nl);
        }
        init(sb.toString());

        @SuppressWarnings("unchecked")
        List<String>[] result = new List[1];

        assertDoesNotThrow(() -> result[0] = parser.expect(expected, ""));
        assertEquals(parsed, result[0]);
    }

    @AfterEach
    public void tearDown() {
        parser = null;
    }

}
