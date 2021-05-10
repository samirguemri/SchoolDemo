package edu.samir.schooldemo.service.authentication.otp;

import edu.samir.schooldemo.persistence.repository.OptRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class OtpService {

    private final OptRepository optRepository;

    public void saveOtp(Otp otp){
        optRepository.save(otp);
    }
}
