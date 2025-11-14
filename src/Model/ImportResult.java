package Model;

// this will give the Import Row, and ignore row count
// Used to summarize how many records were successfully imported or skipped.

public record ImportResult(int importedCount, int ignoredCount) {}

