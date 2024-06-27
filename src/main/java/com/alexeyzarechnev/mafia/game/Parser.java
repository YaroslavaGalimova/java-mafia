package com.alexeyzarechnev.mafia.game;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Parser {

    static final String nl = System.lineSeparator();
    private Scanner scanner;
    private Writer output;

    public Parser(Reader input, Writer output) {
        scanner = new Scanner(input);
        this.output = output;
    }

    private void ask(String message) throws InvalidAlgorithmParameterException {
        try {
            output.write(message);
            output.flush();
        } catch (IOException e) {
            throw new InvalidAlgorithmParameterException("Can't write to output");
        }
    }

    public List<String> expect(List<Answer> expected, String message) throws InvalidAlgorithmParameterException {
        if (expected.size() == 0)
            return List.of();

        ask(message + nl);
        int index = 0;
        List<String> result = new ArrayList<String>();
        while (true) {
            String line;
            try {
                line = scanner.nextLine();
            } catch (NoSuchElementException e) {
                ask("Do not close the input during the game" + nl);
                throw new InvalidAlgorithmParameterException("expect more than input have");
            }
            StringTokenizer tokenizer = new StringTokenizer(line);
            while (tokenizer.hasMoreTokens() && index < expected.size()) {
                String token = tokenizer.nextToken();
                Answer answer = expected.get(index);
                if (!answer.check(token)) {
                    ask("Incorrect argument: '" + token + '\'' + nl);
                    break;
                }
                ++index;
                result.add(token);
            }
            if (result.size() < expected.size())
                continue;

            return result;
        } 
    }
}
