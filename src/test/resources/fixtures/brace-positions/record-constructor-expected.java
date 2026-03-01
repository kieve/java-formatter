public record FormatterTest(int x, int y) {
    public FormatterTest {
        if (x < 0) {
            throw new IllegalArgumentException();
        }
    }
}
