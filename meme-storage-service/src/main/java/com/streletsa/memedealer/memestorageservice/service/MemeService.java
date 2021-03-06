package com.streletsa.memedealer.memestorageservice.service;

import com.streletsa.memedealer.memestorageservice.config.ConstantsConfig;
import com.streletsa.memedealer.memestorageservice.model.Meme;
import com.streletsa.memedealer.memestorageservice.model.User;
import com.streletsa.memedealer.memestorageservice.publisher.MemePublisher;
import com.streletsa.memedealer.memestorageservice.repository.MemeRepository;
import com.streletsa.memedealer.memestorageservice.utils.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MemeService {

    private static final String isNeedToSaveImages = ConstantsConfig.AUTO_SAVE_IMAGES;
    private static final String savingImagesPath = ConstantsConfig.SAVING_IMAGES_PATH;

    @Autowired
    MemeRepository memeRepository;
    @Autowired
    UserService userService;
    @Autowired
    MemePublisher memePublisher;

    @PostConstruct
    private void init(){
        MemeLifeMonitor memeLifeMonitor = new MemeLifeMonitor(this);

        memePublisher.start();
        memeLifeMonitor.start();
    }

    public void storeAutomaticallyApprovedMeme(Meme meme){
        try {
            meme.setApproved(true);
            meme.setTimestamp(System.currentTimeMillis());
            memeRepository.insert(meme);
            memePublisher.publishMeme(meme);
        } catch (Exception e){
            log.error("Test meme storing error -> {}", e.getMessage());
        }
    }

    public void storeMeme(Meme meme, @Nullable String token){
        Optional<User> userOptional = userService.getUserByToken(token);
        boolean isApprovedMeme = userOptional.isPresent();
        try {
            meme.setApproved(isApprovedMeme);
            meme.setTimestamp(System.currentTimeMillis());
            memeRepository.insert(meme);

            if (isApprovedMeme) {
                User user = userOptional.get();
                meme.setApprover(user);
                memePublisher.publishMeme(meme);
            }
            if (isNeedToSaveImages.equals("true")){
                ImageUtils.saveImage(meme.getImage(), savingImagesPath);
            }
        } catch (Exception e){
            log.error("Meme insertion error -> {}", e.getMessage());
        }

    }

    public void storeMeme(Meme meme){
        storeMeme(meme, null);
    }

    public void storeMemeList(List<Meme> memeList, @Nullable String token){
        for (Meme meme : memeList){
            storeMeme(meme, token);
        }
    }

    public List<Meme> getAllApprovedMemes(){
        List<Meme> memeList = new ArrayList<>();
        try {
            memeList = memeRepository.findByApproved(true);
        } catch (Exception e){
            log.error("Meme list reading error -> {}", e.getMessage());
        }

        return memeList;
    }

    public List<Meme> getAllUnapprovedMemes(){
        List<Meme> memeList = new ArrayList<>();
        try {
            memeList = memeRepository.findByApproved(false);
        } catch (Exception e){
            log.error("Unapproved meme list reading error -> {}", e.getMessage());
        }

        return memeList;
    }

    public List<Meme> getUnapprovedMemesWithLimit(Integer limit){
        List<Meme> memeList = new ArrayList<>();
        try {
            memeList = memeRepository
                    .findByApproved(false)
                    .stream()
                    .limit(limit)
                    .collect(Collectors.toList());
        } catch (Exception e){
            log.error("Unapproved meme list reading with limit error -> {}", e.getMessage());
        }

        return memeList;
    }

    public List<Meme> getAllMemes(String userToken){

        List<Meme> memeList = new ArrayList<>();

        if (!userService.checkAccessToken(userToken)){
            return memeList;
        }

        try {
            memeList = memeRepository.findAllByOrderByTimestampDesc();
        } catch (Exception e){
            log.error("Meme list reading error -> {}", e.getMessage());
        }

        return memeList;
    }

    public List<Meme> getMemesWhereTimestampGreaterThan(Long timestamp){
        return memeRepository.findByTimestampGreaterThanEqualOrderByTimestampDesc(timestamp);
    }

    public List<Meme> getMemesWhereTimestampGreaterThan(Long timestamp, Integer limit){
        return getMemesWithLimit(
                getMemesWhereTimestampGreaterThan(timestamp),
                limit
        );
    }

    public List<Meme> getMemesWithLimit(long limit){
        List<Meme> memeList = memeRepository.findAllByOrderByTimestampDesc()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
        return memeList;
    }

    public List<Meme> getMemesWithLimit(List<Meme> memeList, long limit){
        return memeList
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public void updateMeme(Meme meme){
        memeRepository.deleteById(meme.getId());
        memeRepository.insert(meme);
    }

    public void deleteMemeList(List<Meme> memeList){
        for (Meme meme : memeList){
            String memeId = meme.getId();
            deleteMemeById(memeId);
        }
    }

    public void deleteMemeById(String id){
        memeRepository.deleteById(id);
    }

    public void approveMemeList(List<String> memeIdList, String userToken){
        for (String memeId : memeIdList){
            approveMeme(memeId, userToken);
        }
    }

    public void approveMeme(String id, String userToken){
        Optional<User> userOptional = userService.getUserByToken(userToken);
        Optional<Meme> memeOptional = memeRepository.findById(id);

        if (userOptional.isPresent() && memeOptional.isPresent()){
            User user = userOptional.get();
            Meme meme = memeOptional.get();

            meme.setApproved(true);
            meme.setApprover(user);
            meme.setTimestamp(System.currentTimeMillis());

            updateMeme(meme);
            memePublisher.publishMeme(meme);
        }
    }

}
