package com.mabawa.nnpdairy.services;

import com.google.gson.Gson;
import com.mabawa.nnpdairy.models.Trainings;
import com.mabawa.nnpdairy.models.mongo.TMaterials;
import com.mabawa.nnpdairy.repository.TrainersRepository;
import com.mabawa.nnpdairy.repository.mongo.TMaterialRepository;
import com.mabawa.nnpdairy.services.mongo.TMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TrainingsService {
    @Autowired
    private TrainersRepository trainersRepository;

    @Autowired
    private TMaterialService tMaterialService;

    @Autowired
    private Gson gson;

    public List<Trainings> getAllTraining(){
        List<Trainings> trainingsList = trainersRepository.findAll();
        trainingsList.forEach(trainings -> {
            String trainingsId = trainings.getId().toString();
            trainings.settMaterials(tMaterialService.getTrainingTMaterials(trainingsId));
        });
        return  trainingsList;
    }

    public List<Trainings> getAllTrainingWithImagesList(){
        List<Trainings> trainingsList = trainersRepository.findAll();
        trainingsList.forEach(trainings -> {
            String trainingsId = trainings.getId().toString();
            trainings.setImagesDownload(tMaterialService.getTrainingTMaterialsImages(trainingsId));
        });
        return  trainingsList;
    }

    public Optional<Trainings> findById(UUID id){
        return  trainersRepository.findById(id);
    }

    public Optional<Trainings> getTrainingByDescription(String descr){
        return trainersRepository.getTrainingsByDescription(descr);
    }

    public List<Trainings> filterTrainingsByCategoryID(UUID category){
        return trainersRepository.findTrainingsByCategory(category);
    }

    public Trainings create(Trainings trainings){
        return  trainersRepository.saveAndFlush(trainings);
    }

    public Trainings update(Trainings trainings) {
        return trainersRepository.save(trainings);
    }

    public void deleteTrainingById(UUID tId){
        trainersRepository.deleteTrainingsById(tId);
    }

    public Trainings getJson(String training){
        Trainings trainings = gson.fromJson(training, Trainings.class);

        return trainings;
    }
}
