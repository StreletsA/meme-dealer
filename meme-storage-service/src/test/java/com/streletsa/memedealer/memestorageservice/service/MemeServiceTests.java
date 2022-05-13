package com.streletsa.memedealer.memestorageservice.service;

import com.streletsa.memedealer.memestorageservice.model.Meme;
import com.streletsa.memedealer.memestorageservice.publisher.MemePublisher;
import com.streletsa.memedealer.memestorageservice.repository.MemeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MemeServiceTests {

    @Mock
    MemeRepository memeRepository;
    @Mock
    MemePublisher memePublisher;

    @InjectMocks
    MemeService memeService;


    @Test
    public void storeAutomaticallyApprovedMemeWithRepositoryErrorTest(){
        Meme meme = new Meme();
        Mockito.when(memeRepository.insert(Mockito.<Meme>any())).thenThrow(new RuntimeException());


        memeService.storeAutomaticallyApprovedMeme(meme);

        // No exceptions

    }

}
