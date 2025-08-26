public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        isDone = false;
    }

    public String getDescription() {
         return (isDone ? "[X] " : "[ ] ") + description;
    }

    public Task mark() {
        isDone = true;
        return this;
    }

    public Task unmark() {
        isDone = false;
        return this;
    }

    /** Escape pipe so file format is safe. */
    protected static String esc(String s) {
        return s.replace("\\", "\\\\").replace("|", "\\|");
    }
    protected static String unesc(String s) {
        StringBuilder out = new StringBuilder();
        boolean esc = false;
        for (char c : s.toCharArray()) {
            if (esc) { out.append(c); esc = false; }
            else if (c == '\\') esc = true;
            else out.append(c);
        }
        return out.toString();
    }

    /** Subclasses must return a line like: "T | 1 | desc" or "D | 0 | desc | extra" */
    public abstract String toFileString();

    /** Parse flexible input: splits on '|' then trims each part; supports 3 or 4 (and legacy 5) fields. */
    public static Task fromFileString(String line) {
        // split on '|' (not " | ") then trim, to accept both with/without spaces
        String[] raw = line.split("\\|", -1);
        java.util.ArrayList<String> parts = new java.util.ArrayList<>();
        for (String r : raw) parts.add(r.trim());
        if (parts.size() < 3) throw new IllegalArgumentException("Corrupt line: " + line);

        String type = parts.get(0);
        boolean done = "1".equals(parts.get(1));
        String desc = unesc(parts.get(2));
        String extra1 = parts.size() > 3 ? unesc(parts.get(3)) : "";
        //String extra2 = parts.size() > 4 ? unesc(parts.get(4)) : "";

        Task t;
        switch (type) {
            case "T":
                t = new Todo(desc);
                break;
            case "D":
                // deadline wants one extra: by
                t = new Deadline(desc, extra1);
                break;
            case "E":
                // your current Event has (from, to). The file has ONE extra ("Aug 6th 2-4pm").
                // We'll store whole string in 'from' and leave 'to' empty.
                t = new Event(desc, extra1, "");
                break;
            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }
        if (done) t.mark();
        return t;
    }
}
