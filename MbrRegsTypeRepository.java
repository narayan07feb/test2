package com.optum.ogn.persistence;

import com.optum.ogn.domain.Mbr;
import com.optum.ogn.domain.MbrRgstTyp;
import com.optum.ogn.exceptions.GlobalNavException;

public interface MbrRegsTypeRepository extends DomainObjectRepository<MbrRgstTyp> {

	MbrRgstTyp findMbrRegistrationType(Mbr mbr) throws GlobalNavException;
	MbrRgstTyp findMbrRegistrationTypeId(String registrationType) throws GlobalNavException;
}

