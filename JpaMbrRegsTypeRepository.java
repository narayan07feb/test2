package com.optum.ogn.persistence;

import com.optum.ogn.constants.Constants;
import com.optum.ogn.domain.Mbr;
import com.optum.ogn.domain.MbrPrtl;
import com.optum.ogn.domain.MbrRgstTyp;
import com.optum.ogn.exceptions.GlobalNavException;
import com.optum.ogn.exceptions.MessageType;
import com.optum.ogn.exceptions.MessagesType;
import com.optum.ogn.exceptions.StatusType;

@Repository
public class JpaMbrRegsTypeRepository extends JpaRepository<MbrRgstTyp> implements MbrRegsTypeRepository  {
	private static final Logger LOGGER = LoggerFactory.getLogger(JpaMemberPortalRepository.class);
	
	
	@Override
	public MbrRgstTyp findMbrRegistrationType(Mbr mbr) throws GlobalNavException {

		try {

			LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName()+" :: START");
			String	queryString=null;//,mbrQuery=null;
			queryString= "SELECT mbrRgst FROM MbrRgstTyp mbrRgst where mbrRgst.mbrRgstTypId= ?1";
			
			Query query = em.createQuery(queryString);
			query.setParameter(1,mbr.getMbrRgstTypId());
			LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName()+" :: END");
			return (MbrRgstTyp)query.getSingleResult();

		} catch(Exception  ex)
		{
//			ex.printStackTrace();
			MessagesType mts=new MessagesType();
			MessageType mt=new MessageType();
			mt.setCode("500");
			mt.setSeverity(Constants.Severity.EXCEPTION.name());
			mt.setName(Constants.StatusMsgNm.NOTFOUND.name());
			StringBuilder sb=new StringBuilder();

			sb.append("Exception in lookup member registration type table with registration type Id\n");
			sb.append("RegistrationTypeId:'").append(mbr.getMbrRgstTypId()).append("'\n");
			
			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			StatusType st=new StatusType();
			st.setMessages(mts);
			
			throw new GlobalNavException(st);
		}
	}

	// new method added
	@Override
	public MbrRgstTyp findMbrRegistrationTypeId(String registrationType) throws GlobalNavException {
		// TODO Auto-generated method stub
		try {

			LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName()+" :: START");
			String	queryString=null;//,mbrQuery=null;
			queryString= "SELECT mbrRgst FROM MbrRgstTyp mbrRgst where mbrRgst.mbrRgstTypDesc= ?1";
			
			Query query = em.createQuery(queryString);
			query.setParameter(1,registrationType);
			LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName()+" :: END");
			return (MbrRgstTyp)query.getSingleResult();

		} catch(Exception  ex)
		{
//			ex.printStackTrace();
			MessagesType mts=new MessagesType();
			MessageType mt=new MessageType();
			mt.setCode("500");
			mt.setSeverity(Constants.Severity.EXCEPTION.name());
			mt.setName(Constants.StatusMsgNm.NOTFOUND.name());
			StringBuilder sb=new StringBuilder();

			sb.append("Exception in lookup member registration type table with registration type Id\n");
			sb.append("RegistrationType:'").append(registrationType).append("'\n");
			
			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			StatusType st=new StatusType();
			st.setMessages(mts);
			
			throw new GlobalNavException(st);
		}
	}


}
