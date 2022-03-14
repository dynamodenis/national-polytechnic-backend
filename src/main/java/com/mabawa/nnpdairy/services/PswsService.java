package com.mabawa.nnpdairy.services;

import com.mabawa.nnpdairy.models.Psws;
import com.mabawa.nnpdairy.repository.PswsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PswsService {
    @Autowired
    private PswsRepository pswsRepository;

    public Optional<Psws> getByUserId(UUID userId){
        return pswsRepository.getPswsByUserid(userId);
    }

    public Psws create(Psws psws){
        return pswsRepository.saveAndFlush(psws);
    }

    public void updatePsws(Timestamp last, String psw1, String psw2, String psw3, String psw4, String psw5, UUID userid){
        pswsRepository.updatePsws(last, psw1, psw2, psw3, psw4, psw5, userid);
    }

    public void deleteByUserId(UUID userId)
    {
        pswsRepository.deletePsws(userId);
    }

    public Psws getUserPasswords(UUID userId, String psw){
        LocalDateTime lastLocal = LocalDateTime.now();
        Psws psws = new Psws();
        Optional<Psws> optionalPsws = pswsRepository.getPswsByUserid(userId);
        if (!optionalPsws.isPresent()) {
            psws.setUserid(userId);
            psws.setLastdate(Timestamp.valueOf(lastLocal));
            psws.setPsw1(psw);
            psws.setPsw2("");
            psws.setPsw3("");
            psws.setPsw4("");
            psws.setPsw5("");
            psws.setLastpsw(0);
        } else {
            psws = optionalPsws.get();
        }

        return psws;
    }
}
