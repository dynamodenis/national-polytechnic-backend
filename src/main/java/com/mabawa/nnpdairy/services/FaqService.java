package com.mabawa.nnpdairy.services;

import com.mabawa.nnpdairy.models.Faq;
import com.mabawa.nnpdairy.repository.FaqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FaqService {
    @Autowired
    private FaqRepository faqRepository;

    public List<Faq> getAllFaq()
    {
        return faqRepository.findAll();
    }

    public Optional<Faq> findFaqById(UUID id)
    {
        return faqRepository.findById(id);
    }

    public List<Faq> findNameContaining(String qstn)
    {
        return faqRepository.findByQuestionContainingIgnoreCase(qstn);
    }

    public Faq create(Faq faq)
    {
        return faqRepository.saveAndFlush(faq);
    }

    public Faq update(Faq faq)
    {
        return faqRepository.saveAndFlush(faq);
    }

    public void deleteFaqById(UUID id)
    {
        faqRepository.deleteFaqById(id);
    }
}
