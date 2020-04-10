import java.util.stream.IntStream;

/**
 * @author hiscat
 */
public class StreamTest {
    public static void main(String[] args) {
        IntStream.rangeClosed(1, 9)
                .forEach(i -> System.out.printf("%s%s\n", " ".repeat(9 - i), "*".repeat(2 * i - 1)));
    }
}
