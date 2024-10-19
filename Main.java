import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Main {

    public static void main(String[] args) {
        try {
            // Caminho do arquivo CSV
            String filePath = "src//Academy_Candidates.csv";
            List<Candidate> candidates = readCandidatesFromCSV(filePath);

            // 1. Proporção de candidatos que se inscreveram para cada vaga
            Map<String, Long> vagaCounts = candidates.stream()
                    .collect(Collectors.groupingBy(Candidate::getVaga, Collectors.counting()));
            vagaCounts.forEach((vaga, count) -> {
                double percentage = (count * 100.0) / candidates.size();
                System.out.println("Vaga: " + vaga + " - " + String.format("%.2f", percentage) + "%");
            });

            // 2. Idade média dos candidatos de QA
            double idadeMediaQA = candidates.stream()
                    .filter(c -> c.getVaga().equals("QA"))
                    .collect(Collectors.averagingInt(Candidate::getIdade));
            System.out.println("Idade média dos candidatos de QA: " + String.format("%.2f", idadeMediaQA));

            // 3. Idade do candidato mais velho de Mobile
            Optional<Candidate> candidatoMaisVelhoMobile = candidates.stream()
                    .filter(c -> c.getVaga().equals("Mobile"))
                    .max(Comparator.comparingInt(Candidate::getIdade));
            candidatoMaisVelhoMobile.ifPresent(c -> 
                    System.out.println("Idade do candidato mais velho de Mobile: " + c.getIdade()));

            // 4. Idade do candidato mais novo de Web
            Optional<Candidate> candidatoMaisNovoWeb = candidates.stream()
                    .filter(c -> c.getVaga().equals("Web"))
                    .min(Comparator.comparingInt(Candidate::getIdade));
            candidatoMaisNovoWeb.ifPresent(c -> 
                    System.out.println("Idade do candidato mais novo de Web: " + c.getIdade()));

            // 5. Soma das idades dos candidatos de QA
            int somaIdadesQA = candidates.stream()
                    .filter(c -> c.getVaga().equals("QA"))
                    .mapToInt(Candidate::getIdade)
                    .sum();
            System.out.println("Soma das idades dos candidatos de QA: " + somaIdadesQA);

            // 6. Número de estados distintos entre os candidatos
            long numEstadosDistintos = candidates.stream()
                    .map(Candidate::getEstado)
                    .distinct()
                    .count();
            System.out.println("Número de estados distintos: " + numEstadosDistintos);

            // 7. Criar arquivo com candidatos ordenados por idade
            List<Candidate> sortedCandidates = candidates.stream()
                    .sorted(Comparator.comparing(Candidate::getIdade))
                    .collect(Collectors.toList());
            writeCandidatesToCSV("Sorted_Academy_Candidates.csv", sortedCandidates);
            System.out.println("Arquivo 'Sorted_Academy_Candidates.csv' foi criado com sucesso.");

            // 8. Encontrar instrutor de QA
            Optional<Candidate> instrutorQA = candidates.stream()
                    .filter(c -> c.getVaga().equals("QA"))
                    .filter(c -> c.getEstado().equals("SC"))
                    .filter(c -> isPerfectSquare(c.getIdade()) && c.getIdade() >= 18 && c.getIdade() <= 30)
                    .filter(c -> isPalindrome(c.getNome().split(" ")[0]))
                    .findFirst();
            instrutorQA.ifPresent(qa -> System.out.println("Instrutor de QA: " + qa.getNome()));

            // 9. Encontrar instrutor de Mobile
            Optional<Candidate> instrutorMobile = candidates.stream()
                    .filter(c -> c.getVaga().equals("Mobile"))
                    .filter(c -> c.getEstado().equals("PI"))
                    .filter(c -> c.getIdade() % 2 == 0 && c.getIdade() >= 30 && c.getIdade() <= 40)
                    .filter(c -> c.getNome().split(" ")[1].startsWith("C"))
                    .findFirst();
            instrutorMobile.ifPresent(mobile -> System.out.println("Instrutor de Mobile: " + mobile.getNome()));

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    // Função para ler os dados do CSV
    public static List<Candidate> readCandidatesFromCSV(String filePath) throws IOException {
        List<Candidate> candidates = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        for (String line : lines.subList(1, lines.size())) {  // Ignorar o cabeçalho
            String[] fields = line.split(",");
            String nome = fields[0].trim();
            int idade = Integer.parseInt(fields[1].replace(" anos", "").trim());
            String vaga = fields[2].trim();
            String estado = fields[3].trim();
            candidates.add(new Candidate(nome, idade, vaga, estado));
        }
        return candidates;
    }

    // Função para escrever os candidatos ordenados em um arquivo CSV
    public static void writeCandidatesToCSV(String filePath, List<Candidate> candidates) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("Nome,Idade,Vaga,Estado");
        for (Candidate candidate : candidates) {
            lines.add(candidate.toString());
        }
        Files.write(Paths.get(filePath), lines);
    }

    // Verifica se um número é um quadrado perfeito
    public static boolean isPerfectSquare(int num) {
        int sqrt = (int) Math.sqrt(num);
        return sqrt * sqrt == num;
    }

    // Verifica se uma palavra é um palíndromo
    public static boolean isPalindrome(String word) {
        return word.equalsIgnoreCase(new StringBuilder(word).reverse().toString());
    }
}

// Classe Candidate
class Candidate {
    String nome;
    int idade;
    String vaga;
    String estado;

    public Candidate(String nome, int idade, String vaga, String estado) {
        this.nome = nome;
        this.idade = idade;
        this.vaga = vaga;
        this.estado = estado;
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    public String getVaga() {
        return vaga;
    }

    public String getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return nome + "," + idade + "," + vaga + "," + estado;
    }
}
