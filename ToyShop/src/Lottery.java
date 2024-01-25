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
//        PriorityQueue<Toy> tempQueue = new PriorityQueue<>(this.toys);
        this.toysFrequency = new HashMap<>();
        for (Toy toy : this.toys) {
            this.toysFrequency.putIfAbsent(toy.getName(), 0);
        }
//        while (!tempQueue.isEmpty()) {
////            toysFrequency.put(tempQueue.poll().getName(),0);
//            this.toysFrequency.putIfAbsent(tempQueue.poll().getName(), 1);
//        }
//        System.out.println(this.toysFrequency);
        computeFrequency();
    }

    private void computeFrequency() {
//        PriorityQueue<Toy> tempQueue = new PriorityQueue<>(this.toys);
//        while (!tempQueue.isEmpty()) {
////            toysFrequency.put(tempQueue.poll().getName(),0);
//            Toy temp = tempQueue.poll();
//            if (tempQueue.contains(temp)){
//                int value = this.toysFrequency.get(temp.getName()) + 1;
//                this.toysFrequency.put(temp.getName(), value);
//            }
//        }
        for (Toy toy : this.toys) {
            this.toysFrequency.put(toy.getName(), this.toysFrequency.get(toy.getName()) + 1);
        }
//        System.out.println(this.toysFrequency);
    }

    private void updateRateForOneToy() {
        this.toys.forEach(x -> x.setRate((float) 1 / this.toys.size()));
//        for (Toy toy:this.toys){
////            toy.setRate((float)this.toysFrequency.get(toy.getName())/toys.size());
//            toy.setRate((float) 1 / this.toys.size());
//            toysWithTrueRate.offer(toy);
//        }
    }

//    public Toy getToy(){
//        Set<Toy> tempToys = new HashSet<Toy>();
//        PriorityQueue<Toy> tempQueue = new PriorityQueue<>(this.toysWithTrueRate);
//        Toy returnToy;
//        double rollRate = roll();
//        while (!tempQueue.isEmpty()){
//            Toy tempToy = tempQueue.poll();
//            if (tempToy.getRate() <= rollRate){
//                tempToys.add(tempToy);
//            }
//        }
//        if (tempToys.size() > 1){
//            returnToy = (Toy) tempToys.toArray()[new Random().nextInt(0,tempToys.size())];
//        }else returnToy =  (Toy)tempToys.toArray()[0];
//        //TODO ����� ��������� ���������� outOfBounds
//        removeToyFromDatabase(returnToy);
//        return returnToy;
//    }

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
            sb.append("�� ��������: ").append(toy.getName()).append(" ����������� ���������: ").append(thisToyRate);
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