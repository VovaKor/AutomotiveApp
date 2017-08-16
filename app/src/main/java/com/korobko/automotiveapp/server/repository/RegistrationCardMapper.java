package com.korobko.automotiveapp.server.repository;

import android.support.annotation.NonNull;

import com.korobko.automotiveapp.models.EntityRegCard;
import com.korobko.automotiveapp.models.RegistrationCard;

/**
 * Created by vova on 16.08.17.
 * Converts RegistrationCard to EntityRegCard and vise versa
 */

public class RegistrationCardMapper {
    public static RegistrationCard fromInternal(@NonNull EntityRegCard entityRegCard){
        RegistrationCard card = new RegistrationCard();
        card.setRegistrationNumber(entityRegCard.getRegistrationNumber());
        card.setDriver(entityRegCard.getDriver());
        card.setCars(entityRegCard.getCars());
        return card;
    }
    public static EntityRegCard toInternal(@NonNull RegistrationCard registrationCard){
        EntityRegCard entityRegCard = new EntityRegCard();
        entityRegCard.setRegistrationNumber(registrationCard.getRegistrationNumber());
        entityRegCard.setDriver(registrationCard.getDriver());
        entityRegCard.setId_driver(registrationCard.getDriver().getId());
        entityRegCard.setCars(registrationCard.getCars());
        return entityRegCard;
    }
}
