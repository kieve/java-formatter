public sealed class FormatterTest
    permits
    SubclassAlpha,
    SubclassBeta,
    SubclassGamma,
    SubclassDelta {
}

final class SubclassAlpha extends FormatterTest {
}

final class SubclassBeta extends FormatterTest {
}

final class SubclassGamma extends FormatterTest {
}

final class SubclassDelta extends FormatterTest {
}
