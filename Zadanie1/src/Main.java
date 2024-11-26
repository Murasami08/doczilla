
import java.io.*;
import java.nio.file.*;
import java.util.*;
public class Main {
    private Map<String, List<String>> dependencies = new HashMap<>();
    private Set<String> visited = new HashSet<>();
    private Set<String> inStack = new HashSet<>();
    private List<String> sortedFiles = new ArrayList<>();
    private String rootPath;
    public Main(String rootPath) {
        this.rootPath = rootPath;
    }
    public static void main(String[] args) {
        String rootPath = "C:\\Users\\Admin\\Desktop\\Zadanie1";
        Main fileMerger = new Main(rootPath);
        fileMerger.collectFiles(new File(rootPath));
        fileMerger.resolveDependencies();
        try {
            fileMerger.topologicalSort();
            fileMerger.concatenateFiles("output.txt");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    private void collectFiles(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    collectFiles(file);
                } else if (file.getName().endsWith(".txt")) {
                    parseFile(file);
                }
            }
        }
    }
    private void parseFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String currentPath = file.getAbsolutePath().substring(rootPath.length() + 1);
            while ((line = br.readLine()) != null) {
                if (line.startsWith("require")) {
                    String requiredFilePath = extractRequiredFilePath(line);
                    dependencies.computeIfAbsent(currentPath, k -> new ArrayList<>()).add(requiredFilePath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String extractRequiredFilePath(String line) {
        return line.replaceAll("\\require '([^']+)'", "$1").trim();
    }
    private void resolveDependencies() {
        for (String file : dependencies.keySet()) {
            if (!visited.contains(file)) {
                visit(file);
            }
        }
    }
    private void visit(String file) {
        if (inStack.contains(file)) {
            throw new RuntimeException("Циклическая зависимость: " + findCycle(file));
        }
        if (visited.contains(file)) {
            return;
        }

        inStack.add(file);
        visited.add(file);

        List<String> deps = dependencies.getOrDefault(file, Collections.emptyList());
        for (String dep : deps) {
            visit(dep);
        }

        inStack.remove(file);
        sortedFiles.add(file);
    }
    private List<String> findCycle(String startFile) {
        List<String> cycle = new ArrayList<>();
        Set<String> cycleCheck = new HashSet<>();
        buildCycle(startFile, cycle, cycleCheck);
        return cycle;
    }
    private void buildCycle(String file, List<String> cycle, Set<String> cycleCheck) {
        if (cycleCheck.contains(file)) {
            cycle.add(file);
            return;
        }
        cycleCheck.add(file);
        for (String dep : dependencies.getOrDefault(file, Collections.emptyList())) {
            buildCycle(dep, cycle, cycleCheck);
        }
        cycleCheck.remove(file);
    }

    private void topologicalSort() {
        Collections.reverse(sortedFiles);
    }
    private void concatenateFiles(String outputFilePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            for (String file : sortedFiles) {
                File f = new File(rootPath, file);
                try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
        }
    }


}