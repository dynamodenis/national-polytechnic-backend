package com.mabawa.nnpdairy.repository;

import com.mabawa.nnpdairy.models.MessagesSent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageSentRepository extends JpaRepository<MessagesSent, Long> {
}
