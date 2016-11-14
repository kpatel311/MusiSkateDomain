
 //Random forest with information gain
        RandomForest rf = new RandomForest();
        rf.setNumTrees(300);
        rf.buildClassifier(inst);

        // Apply information gain to the features

        AttributeSelection as = new AttributeSelection();
        Ranker ranker = new Ranker();

        InfoGainAttributeEval infoGainAttrEval = new InfoGainAttributeEval();
        as.setEvaluator(infoGainAttrEval);
        as.setSearch(ranker);
        as.setInputFormat(inst);
        inst = Filter.useFilter(inst, as);
        Evaluation eval3 = new Evaluation(inst);
        eval3.crossValidateModel(rf, inst, 9, new Random(1));
        int count = 0;
        // Using HashMap to store the infogain values of the attributes
        Map<Attribute, Double> infogainscores = new HashMap<Attribute, Double>();
        for (int i = 0; i < inst.numAttributes(); i++) {
        Attribute t_attr = inst.attribute(i);
        double infogain  = infoGainAttrEval.evaluateAttribute(i);
        if(infogain != 0){
        infogainscores.put(t_attr, infogain);
        count = count+1;
            }
        }
