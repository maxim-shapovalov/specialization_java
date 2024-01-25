import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Lottery {
    private ArrayList<Toy> toys;
    private Map<String, Integer> toysFrequency;
    private PriorityQueue<Toy> toysWithTrueRate;

    public Lottery() {
        this.toys = new ArrayList<>();
    }

    public void addToy(Toy toy) {
        for (int i = 0; i < toy.getRate(); i++) {
            this.toys.add(toy);
        }
        initFrequencyMap();
    }


    private void initFrequencyMap() {

        this.toysFrequency = new HashMap<>();
        for (Toy toy : this.toys) {
            this.toysFrequency.putIfAbsent(toy.getName(), 0);
        }
        computeFrequency();
    }

    private void computeFrequency() {
        for (Toy toy : this.toys) {
            this.toysFrequency.put(toy.getName(), this.toysFrequency.get(toy.getName()) + 1);
        }
//        System.out.println(this.toysFrequency);
    }

    private void updateRateForOneToy() {
        this.toys.forEach(x -> x.setRate((float) 1 / this.toys.size()));
    }



    public Toy getToy() {
        updateRateForOneToy();
        if (this.toys.size() != 0) {
            int random = new Random().nextInt(0, this.toys.size());
            Toy tempToy = this.toys.get(random);
            writeToFIle(tempToy);
            removeToyFromDatabase(tempToy);
            return tempToy;
        } else return null;
    }

    private void removeToyFromDatabase(Toy toyToRemove) {
        this.toys.remove(toyToRemove);
        this.toysFrequency.put(toyToRemove.getName(), this.toysFrequency.get(toyToRemove.getName()) - 1);
    }

    public double roll() {
        Random random = new Random();
//        double maxBound = (float)this.toysFrequency.values().stream().max(Double::compare).get() / this.toysWithTrueRate.size();
        return random.nextDouble();
    }

    public PriorityQueue<Toy> getToysWithTrueRate() {
        return toysWithTrueRate;
    }

    public Set<Toy> updateOverallRate() {
        updateRateForOneToy();
        Set<Toy> toyRates = new HashSet<>(this.toys);
        for (Toy toy : toyRates) {
            toy.setRate(this.toysFrequency.get(toy.getName()) * toy.getRate());
        }
        return toyRates;
    }

    private String createPath(){
        String path = "rewards.txt";
        File file = new File(path);
        try {
            if (!file.isFile()){
                file.createNewFile();
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return path;
    }

    private void writeToFIle(Toy toy){
        String path = createPath();
        StringBuilder sb = new StringBuilder();
        Set<Toy> overallRate = updateOverallRate();
        double thisToyRate = 0;
        for (Toy temp : overallRate) {
            if (toy.getName().equals(temp.getName())){
                thisToyRate = temp.getRate();
            }
        }
        try (FileWriter fileWriter = new FileWriter(path,true)){
            sb.append("Вы выиграли: ").append(toy.getName()).append(" Вероятность выпадения: ").append(thisToyRate);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(sb.toString() + "\n");
            bufferedWriter.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }



    public int getToysListSize() {
        return toys.size();
    }
}
