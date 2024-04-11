package com.example.ikeguess.customClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class QuizzCreator {
    public ArrayList<FlashCard> listQuizz = new ArrayList<>();

    public ArrayList<FlashCardMaps> listQuizzMaps = new ArrayList<>();



    public void createMultipleSimpleQuizz(DataList dataList, int numberQuestion, String quizzLevel) {
        listQuizz.clear();
        String mediaType = "";
        ArrayList<Data> data = null;
        if ("simple".equals(quizzLevel)){
            mediaType = "image";
            data = dataList.furnitureSimple;
        }
        else if ("medium".equals(quizzLevel)){
            mediaType = "son";
            data = dataList.furnitureMedium;
        }
        ArrayList<Map<String, String>> arrayAnswer = new ArrayList<>();

        Collections.shuffle(data);

        for (int i = 0; i < numberQuestion; i ++ ){
            Map<String, String> mapAnswer = new HashMap<>();

            mapAnswer.put("name", data.get(i).name);
            mapAnswer.put("url", data.get(i).url);

            arrayAnswer.add(mapAnswer);
        }

        ArrayList<ArrayList<String>> arrayQuizz = new ArrayList<>();

        while (arrayQuizz.size() < numberQuestion) {
            ArrayList<String> arrayQuestions = new ArrayList<>();
            arrayQuestions.add(arrayAnswer.get(arrayQuizz.size()).get("name"));

            while (arrayQuestions.size() < 3) {
                int rand = (int) (Math.random() * data.size());

                while (arrayQuestions.contains(data.get(rand).name)) {
                    rand = (int) (Math.random() * data.size());
                }

                arrayQuestions.add(data.get(rand).name);
                Collections.shuffle(arrayQuestions);
            }
            arrayQuizz.add(arrayQuestions);
        }


        int questionsDone = 0;
        while (questionsDone < numberQuestion){
            FlashCard flashCard = new FlashCard("Quel est le nom de ce meuble ?", arrayAnswer.get(questionsDone).get("url"), arrayAnswer.get(questionsDone).get("name"), arrayQuizz.get(questionsDone), numberQuestion, mediaType);
            questionsDone ++;
            listQuizz.add(flashCard);
        }
    }


    public void createMapsQuizz(DataList dataList, int numberQuestion){
        listQuizzMaps.clear();
        Collections.shuffle(dataList.furnitureMaps);
        for (int i = 0; i < numberQuestion; i++) {
            DataMaps data = dataList.furnitureMaps.get(i);
            FlashCardMaps flashCardMaps = new FlashCardMaps(data.latitude, data.longitude, data.url, data.name, data.type, data.radius);
            listQuizzMaps.add(flashCardMaps);
        }
    }
}