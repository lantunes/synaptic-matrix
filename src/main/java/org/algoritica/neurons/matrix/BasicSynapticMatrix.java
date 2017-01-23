package org.algoritica.neurons.matrix;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class BasicSynapticMatrix implements SynapticMatrix<BasicSynapticMatrix> {

    private final SynapticConfig config;
    private final List<InputCell> inputCells;
    private final List<ClassCell> classCells;

    public BasicSynapticMatrix(int numInputCells, int initialNumClassCells, SynapticConfig config) {
        if (numInputCells < 1) {
            throw new IllegalArgumentException("there must be at least 1 input cell");
        }

        this.inputCells = new ArrayList<>(numInputCells);
        this.classCells = new ArrayList<>(initialNumClassCells);
        this.config = config;

        construct(numInputCells, initialNumClassCells);
    }

    private void construct(int numInputCells, int initialNumClassCells) {
        for (int i = 0; i < numInputCells; i++) {
            inputCells.add(new InputCell("#" + (i+1)));
        }
        for (int c = 0; c < initialNumClassCells; c++) {
            addNewClassCell();
        }
    }

    private void addNewClassCell() {
        ClassCell newClassCell = new ClassCell("#" + (classCells.size() + 1));
        Synapse prev = null;
        for (int i = 0; i < inputCells.size(); i++) {
            Synapse curr = new Synapse(config.b(), inputCells.get(i).getId(), newClassCell.getId());
            curr.setPrevious(prev);
            if (prev != null) {
                prev.setNext(curr);
            }
            inputCells.get(i).addAxonalSynapse(curr);
            newClassCell.addDendriticSynapse(curr);
            prev = curr;
        }
        classCells.add(newClassCell);
    }

    @Override
    public int train(int[] inputExample) {
        addNewClassCell();
        int classCell = classCells.size() - 1;
        train(inputExample, classCell);
        return classCell;
    }

    @Override
    public void train(int[] inputExample, int classCell) {
        if (inputExample.length != inputCells.size()) {
            throw new IllegalArgumentException("the number of inputs in example " + classCell +
                    " does not equal the number of input cells");
        }

        List<Synapse> eligibleSynapses = new ArrayList<>();
        for (int i = 0; i < inputExample.length; i++) {
            InputCell inputCell = inputCells.get(i);
            int input = inputExample[i];
            Synapse synapse = inputCell.getSynapse(classCell);
            if (input * synapse.getWeight() > 0) {
                eligibleSynapses.add(synapse);
            }
        }
        if (eligibleSynapses.size() > 0) {
            long kPerN = config.k() / eligibleSynapses.size();
            for (Synapse synapseToUpdate : eligibleSynapses) {
                long weight = synapseToUpdate.getWeight() + config.c() + kPerN;
                synapseToUpdate.setWeight(weight, weight);
                addContributionFromDendrodendriticActivation(synapseToUpdate, weight,
                        config.dendrodendriticContributionLevel(), weight);
            }
        }
    }

    private void addContributionFromDendrodendriticActivation(Synapse synapseToUpdate, long weight, int level, long maxWeight) {
        updatePrev(synapseToUpdate, weight, level, new ArrayList<>(), maxWeight);
        updateNext(synapseToUpdate, weight, level, new ArrayList<>(), maxWeight);
    }

    private void updatePrev(Synapse synapseToUpdate, long weight, int level, List<Synapse> history, long maxWeight) {
        if (synapseToUpdate.previous().isPresent() && level > 0) {
            Synapse previous = synapseToUpdate.previous().get();
            previous.setWeight(previous.getWeight() + (weight / 2), maxWeight);
            for (Synapse h : history) {
                h.setWeight(h.getWeight() + (weight / 2), maxWeight);
            }
            if (level > 1) {
                history.add(previous);
                updatePrev(previous, (weight / 2), level - 1, history, maxWeight);
            }
        }
    }

    private void updateNext(Synapse synapseToUpdate, long weight, int level, List<Synapse> history, long maxWeight) {
        if (synapseToUpdate.next().isPresent() && level > 0) {
            Synapse next = synapseToUpdate.next().get();
            next.setWeight(next.getWeight() + (weight / 2), maxWeight);
            for (Synapse h : history) {
                h.setWeight(h.getWeight() + (weight / 2), maxWeight);
            }
            if (level > 1) {
                history.add(next);
                updateNext(next, (weight / 2), level - 1, history, maxWeight);
            }
        }
    }

    @Override
    public int[] evaluate(int[] input) {
        if (input.length != inputCells.size()) {
            throw new IllegalArgumentException("the number of inputs does not equal the number of input cells");
        }
        return classCells.stream().parallel()
                .mapToInt(classCell -> classCell.activate(input))
                .toArray();
    }

    public int maxClassCellNumber(int[] input) {
        if (input.length != inputCells.size()) {
            throw new IllegalArgumentException("the number of inputs does not equal the number of input cells");
        }
        if (classCells.isEmpty()) return -1;
        return IntStream.range(0, classCells.size()).parallel()
                .mapToObj(i -> new ActivityAndClass(classCells.get(i).activate(input), i))
                .max((o1, o2) -> {
                    if (o1.getActivity() > o2.getActivity()) return 1;
                    if (o1.getActivity() < o2.getActivity()) return -1;
                    return 0;
                }).get().getClassCellNumber();
    }

    @Override
    public JSONObject toJSON() {
        JSONObject root = new JSONObject();

        JSONObject configJSON = new JSONObject();
        configJSON.put("b", config.b());
        configJSON.put("c", config.c());
        configJSON.put("k", config.k());
        configJSON.put("dendrodendriticContributionLevel", config.dendrodendriticContributionLevel());
        root.put("config", configJSON);

        JSONArray weights = new JSONArray();
        for (int c = 0; c < classCells.size(); c++) {
            JSONArray classCellArray = new JSONArray();
            for (Synapse s : classCells.get(c).getDendriticSynapses()) {
                classCellArray.put(s.getWeight());
            }
            weights.put(classCellArray);
        }
        root.put("weights", weights);

        return root;
    }

    @Override
    public BasicSynapticMatrix fromJSON(JSONObject serializationFormat) {
        return null;
    }
}

