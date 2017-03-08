<?xml version="1.0" encoding="UTF-8"?>
<xsd:schema xmlns:xsd="http://www.w3.org/2001/XMLSchema" targetNamespace="http://ogn.optum.com/eligibilitySchema" xmlns:tns="http://ogn.optum.com/eligibilitySchema" elementFormDefault="qualified">
    <xsd:element name="MemberEligibility">
    	<xsd:complexType>
    		<xsd:sequence>
				<xsd:element name="rectype" type="tns:RECTYPE"></xsd:element>
    			<xsd:element name="healthSafeIdFlag" type="xsd:boolean"/>
    			<xsd:element name="homeMentFlag" type="xsd:boolean"/><!-- Added on 5/6/2016 -->
    			<xsd:element name="member" type="tns:MemberType" minOccurs="1" maxOccurs="1"></xsd:element>
				<xsd:element name="portalDestination" type="tns:PortalDestinationType"></xsd:element>
				<xsd:element name="cap" type="tns:CapType" minOccurs="0" maxOccurs="1"></xsd:element>
				<xsd:element name="rx" type="tns:RxType" minOccurs="0" maxOccurs="1"></xsd:element>
				<xsd:element name="laww" type="tns:LawwType"></xsd:element>
				<xsd:element name="optumId" type="tns:OptumIDType" minOccurs="0" maxOccurs="1"></xsd:element>
				<xsd:element name="ccd" type="tns:CCDType" minOccurs="0" maxOccurs="1"></xsd:element>
				<xsd:element name="myuhc" type="tns:MYUHCType" minOccurs="0" maxOccurs="1"></xsd:element>
				<xsd:element name="wageWorks" type="tns:WageWorksType" minOccurs="0" maxOccurs="50"></xsd:element>
				<xsd:element name="wellness" type="tns:PlanType" minOccurs="0" maxOccurs="50"></xsd:element>
				<xsd:element name="hen" type="tns:SubscriberType" minOccurs="0" maxOccurs="50"></xsd:element>
			</xsd:sequence>

				

				<!-- 
				<xsd:element name="hs" type="tns:MYUHCType" minOccurs="0" maxOccurs="1"></xsd:element>
				<xsd:element name="mymedica" type="tns:MYUHCType" minOccurs="0" maxOccurs="1"></xsd:element>
				<xsd:element name="dentalbsca" type="tns:MYUHCType" minOccurs="0" maxOccurs="1"></xsd:element>
				<xsd:element name="myhealthcareview" type="tns:MYUHCType" minOccurs="0" maxOccurs="1"></xsd:element>
				<xsd:element name="communityplan" type="tns:MYUHCType" minOccurs="0" maxOccurs="1"></xsd:element>
				<xsd:element name="harvardpilgrim" type="tns:MYUHCType" minOccurs="0" maxOccurs="1"></xsd:element>
				<xsd:element name="lincolnfinancial" type="tns:MYUHCType" minOccurs="0" maxOccurs="1"></xsd:element>
				<xsd:element name="healthnet" type="tns:MYUHCType" minOccurs="0" maxOccurs="1"></xsd:element>
				<xsd:element name="morganwhite" type="tns:MYUHCType" minOccurs="0" maxOccurs="1"></xsd:element>
				<xsd:element name="confident" type="tns:MYUHCType" minOccurs="0" maxOccurs="1"></xsd:element>
				<xsd:element name="solstice" type="tns:MYUHCType" minOccurs="0" maxOccurs="1"></xsd:element>
				<xsd:element name="healthplex" type="tns:MYUHCType" minOccurs="0" maxOccurs="1"></xsd:element>
				<xsd:element name="goldenrule" type="tns:MYUHCType" minOccurs="0" maxOccurs="1"></xsd:element>
				<xsd:element name="harriscounty" type="tns:MYUHCType" minOccurs="0" maxOccurs="1"></xsd:element>
				<xsd:element name="statefl" type="tns:MYUHCType" minOccurs="0" maxOccurs="1"></xsd:element>
				 -->

　
    	</xsd:complexType>
    </xsd:element>
	<xsd:simpleType name="PortalDestinationType" final="restriction">
		<xsd:restriction base="xsd:string">
			<xsd:enumeration value="CAP" />
			<xsd:enumeration value="LAWW" />
			<xsd:enumeration value="RX" />
			<xsd:enumeration value="DASHBOARD" />
			<xsd:enumeration value="CCD" />
			<xsd:enumeration value="MYUHC" />
			<xsd:enumeration value="HS"/>
			<xsd:enumeration value="MYMEDICA" />
			<xsd:enumeration value="DENTALBSCA" />
			<xsd:enumeration value="MYHEALTHCAREVIEW" />
			<xsd:enumeration value="COMMUNITYPLAN" />
			<xsd:enumeration value="HARVARDPILGRIM"/>
			<xsd:enumeration value="LINCOLNFINANCIAL"/>
			<xsd:enumeration value="HEALTHNET"/>
			<xsd:enumeration value="MORGANWHITE"/>
			<xsd:enumeration value="CONFIDENT"/>
			<xsd:enumeration value="SOLSTICE"/>
			<xsd:enumeration value="HEALTHPLEX"/>
			<xsd:enumeration value="GOLDENRULE"/>
			<xsd:enumeration value="HARRISCOUNTY"/>
			<xsd:enumeration value="STATEFL"/>
			<xsd:enumeration value="GEHUB"/>
			<xsd:enumeration value="WageWorks"/>					
			<xsd:enumeration value="HEN"/>
			<xsd:enumeration value="WCP"/>

		

		</xsd:restriction>
	</xsd:simpleType>
	<xsd:complexType name="OptumIDType">
    	<xsd:sequence>
    		<xsd:element name="healthSafeId" type="xsd:string"></xsd:element>
    		<xsd:element name="firstName" type="xsd:string"></xsd:element>
    		<xsd:element name="middleName" type="xsd:string"></xsd:element>
    		<xsd:element name="lastName" type="xsd:string"></xsd:element>
    		<xsd:element name="suffix" type="xsd:string"></xsd:element>
    		<xsd:element name="birthDate" type="xsd:date"></xsd:element>
    		<xsd:element name="zip" type="xsd:string"></xsd:element>
    		<xsd:element name="phone" type="xsd:string"></xsd:element>
    		<xsd:element name="email" type="xsd:string"></xsd:element>
			<xsd:element name="userId" type="xsd:string"></xsd:element>
    	</xsd:sequence>
	</xsd:complexType>
    <xsd:complexType name="MemberType">
    	<xsd:sequence>
    		<xsd:element name="seqId" type="xsd:string"></xsd:element> <!--  Deprecate in future. Not needed anymore -->
			<xsd:element name="healthSafeIdFlag" type="xsd:boolean"/> <!--  Deprecate in future. This field is moved one level up -->
    		<xsd:element name="healthSafeId" type="xsd:string"></xsd:element> <!--  Deprecate in future. This field is moved OptumID Type -->
    		<xsd:element name="imsId" type="xsd:string"></xsd:element>
    		<xsd:element name="cdbCpin" type="xsd:string"></xsd:element>
    		<xsd:element name="firstName" type="xsd:string"></xsd:element>
    		<xsd:element name="middleName" type="xsd:string"></xsd:element>
    		<xsd:element name="lastName" type="xsd:string"></xsd:element>
    		<xsd:element name="suffix" type="xsd:string"></xsd:element>
    		<xsd:element name="birthDate" type="xsd:date"></xsd:element>
    		<xsd:element name="gender" type="xsd:string"></xsd:element>
    		<xsd:element name="ssn" type="xsd:string"></xsd:element>
    		<xsd:element name="addressLine1" type="xsd:string"></xsd:element>
    		<xsd:element name="addressLine2" type="xsd:string"></xsd:element>
    		<xsd:element name="addressLine3" type="xsd:string"></xsd:element>
    		<xsd:element name="residenceNum" type="xsd:string"></xsd:element>
    		<xsd:element name="city" type="xsd:string"></xsd:element>
    		<xsd:element name="state" type="xsd:string"></xsd:element>
    		<xsd:element name="zip" type="xsd:string"></xsd:element>
    		<xsd:element name="country" type="xsd:string"></xsd:element>
    		<xsd:element name="phone" type="xsd:string"></xsd:element>
    		<xsd:element name="emailAddress" type="xsd:string"></xsd:element>
    		<xsd:element name="policy" type="xsd:string"></xsd:element><!--  Deprecate in future. This field is moved LAWW and CCD Type -->
    		<xsd:element name="subscriberId" type="xsd:string"></xsd:element> <!--  Deprecate in future. This field is moved to LAWW and CCD Type -->
    		<xsd:element name="dependentCode" type="xsd:string"></xsd:element><!--  Deprecate in future -->
			<xsd:element name="employeeId" type="xsd:string"></xsd:element><!--  CAP user don't have ssn -->
			<xsd:element name="groupNumber" type="xsd:string"></xsd:element><!--  CAP user don't have ssn -->
    	</xsd:sequence>
    </xsd:complexType>
    <xsd:complexType name="CapType">
    	<xsd:sequence>
    	    <xsd:element name="lastName" type="xsd:string"></xsd:element>
    		<xsd:element name="altId" type="xsd:string"></xsd:element>
    		<xsd:element name="dateOfBirth" type="xsd:date"></xsd:element>
    		<xsd:element name="financialAccounts" type="tns:FinancialAccountType" minOccurs="0" maxOccurs="50"></xsd:element>
    	</xsd:sequence>
    </xsd:complexType>
    <xsd:complexType name="FinancialAccountType">
    	<xsd:sequence>
   			<xsd:element name="rowId" type="xsd:int"></xsd:element> <!-- Added on 5/3 to indicate row index of the financial account -->
     		<xsd:element name="sourceSysCode" type="xsd:string"></xsd:element>
    		<xsd:element name="id" type="xsd:string"></xsd:element><!-- GetParticipantByIDResponse.AccountHolder.AccountHolderEmployer.Account.AccountIdentifier.OHFSId -->
    		<xsd:element name="accountName" type="xsd:string"></xsd:element>
    		<xsd:element name="accountStatusCode" type="xsd:string"></xsd:element>
    		<xsd:element name="accountStatusDesc" type="xsd:string"></xsd:element>
    		<xsd:element name="accountTypeCode" type="xsd:string"></xsd:element>
    		<xsd:element name="employerBrandingCode" type="xsd:string"></xsd:element> <!--  Added 5/6/2016. Track branding when returning to CAP portal -->
    		<xsd:element name="employerIdentifier" type="xsd:string"></xsd:element> <!--  Added 5/12/2016 <EmployerIdentifier>-->
    		<xsd:element name="accountHolderId" type="xsd:string"></xsd:element><!--  Added 5/23. Contains the account id returned from listParticipant --> 
    		<xsd:element name="effectiveDate" type="xsd:date"></xsd:element> <!-- Added 10/17/2016 for GEHUB -->
    		<xsd:element name="expirationDate" type="xsd:date"></xsd:element> <!-- Added 10/17/2016 for GEHUB -->   		    		
    	</xsd:sequence>
    </xsd:complexType>
    <xsd:complexType name="RxType">
    	<xsd:sequence>
    		<xsd:element name="firstName" type="xsd:string"></xsd:element>
    		<xsd:element name="middleName" type="xsd:string"></xsd:element><!--  Added on 5/6/206 -->
    		<xsd:element name="lastName" type="xsd:string"></xsd:element>
    		<xsd:element name="dateOfBirth" type="xsd:date"></xsd:element>    		
    		<xsd:element name="gender" type="xsd:string"></xsd:element>
    		<xsd:element name="email" type="xsd:string"></xsd:element>
    		<xsd:element name="address" type="xsd:string"></xsd:element> <!-- Deprecate in future. This field has been expanded. -->
    		<xsd:element name="addressLine1" type="xsd:string"></xsd:element> <!--  Added on 5/6/206 -->
    		<xsd:element name="addressLine2" type="xsd:string"></xsd:element> <!--  Added on 5/6/206 -->
    		<xsd:element name="city" type="xsd:string"></xsd:element><!--  Added on 5/6/206 -->
    		<xsd:element name="state" type="xsd:string"></xsd:element><!--  Added on 5/6/206 -->
    		<xsd:element name="zip" type="xsd:string"></xsd:element><!--  Added on 5/6/206 -->
    		<xsd:element name="phone" type="xsd:string"></xsd:element>
    		<xsd:element name="isAuthenticated" type="xsd:boolean"></xsd:element>
    		<xsd:element name="planDetails" type="tns:RxPlanType" minOccurs="0" maxOccurs="50"></xsd:element>    		
    	</xsd:sequence>
    </xsd:complexType>
    <xsd:complexType name="LawwType">
    	<xsd:sequence>
   			<xsd:element name="subscriberIds" type="tns:SubscriberType" minOccurs="0" maxOccurs="50"></xsd:element>
    	</xsd:sequence>
    </xsd:complexType>
    
    <xsd:complexType name="WageWorksType">
    	<xsd:sequence>
   			<xsd:element name="ParticipantID" type="xsd:string"></xsd:element><!-- Added 10/17/2016 for GEHUB -->
    		<xsd:element name="EmployerID" type="xsd:string"></xsd:element><!-- Added 10/17/2016 for GEHUB -->
    	</xsd:sequence>
    </xsd:complexType>
    
    <xsd:complexType name="MYUHCType">
    	<xsd:sequence>
   			<xsd:element name="planDetails" type="tns:PlanType" minOccurs="0" maxOccurs="unbounded"></xsd:element>
    	</xsd:sequence>
    </xsd:complexType>
    <xsd:complexType name="PlanType">
		<xsd:sequence>
   			<xsd:sequence>
   			<xsd:element name="firstName" type="xsd:string"></xsd:element>
    		<xsd:element name="lastName" type="xsd:string"></xsd:element>
    		<xsd:element name="dateOfBirth" type="xsd:date"></xsd:element>
    		<xsd:element name="groupId" type="xsd:string"></xsd:element>
    		<xsd:element name="memberId" type="xsd:string"></xsd:element>
    		<xsd:element name="subscriberId" type="xsd:string"></xsd:element>
    		<xsd:element name="alternateId" type="xsd:string"></xsd:element>
    		<xsd:element name="memberSsn" type="xsd:string"></xsd:element>
    		<xsd:element name="memberType" type="xsd:string"></xsd:element>
    		<xsd:element name="planEffectiveStartDate" type="xsd:date"></xsd:element>
    		<xsd:element name="planEffectiveEndDate" type="xsd:date"></xsd:element> <!-- nullable -->
    		<xsd:element name="groupName" type="xsd:string"></xsd:element>
    		<xsd:element name="idMemberType" type="xsd:string"></xsd:element>
    		<xsd:element name="planEligibilityIndicator" type="xsd:string"></xsd:element>
    		
    		<!-- Additional fields for GE/H4me 2.0 -start -->
    		<xsd:element name="situsState" type="xsd:string"></xsd:element>
    		<xsd:element name="coverageCode" type="xsd:string"></xsd:element>
    		<xsd:element name="relationshipCode" type="xsd:string"></xsd:element>
    		<xsd:element name="globalSolutionsIndicator" type="xsd:string"></xsd:element>
    		<xsd:element name="sourceCode" type="xsd:string"></xsd:element>
    		<xsd:element name="marketNumber" type="xsd:string"></xsd:element>
    		<xsd:element name="marketType" type="xsd:string"></xsd:element>
    		<xsd:element name="obligorId" type="xsd:string"></xsd:element>
    		<xsd:element name="productCode" type="xsd:string"></xsd:element>
    		<xsd:element name="sharedArrangementCode" type="xsd:string"></xsd:element>
    		<xsd:element name="cdbXrefId" type="xsd:string"></xsd:element>
    		<xsd:element name="cdbPartitionNumber" type="xsd:string"></xsd:element>
    		<xsd:element name="governmentProgramTypeCode" type="xsd:string"></xsd:element>   
    		<xsd:element name="planVariation" type="xsd:string"></xsd:element>
    		<xsd:element name="reportingCode" type="xsd:string"></xsd:element>    	
    		<!-- Additional fields for GE/H4me 2.0 -start -->
    		
    		<!-- Additional fields for UHC west issue -start -->
    		<xsd:element name="eligibilitySystemTypeCode" type="xsd:string"></xsd:element>   
    		<xsd:element name="claimSystemTypeCode" type="xsd:string"></xsd:element>
    		<xsd:element name="migrateLegacySourceId" type="xsd:string"></xsd:element>   
    		<xsd:element name="migrateLegacyPolicyNumber" type="xsd:string"></xsd:element>
    		<!-- Additional fields for UHC west issue -start -->
    	</xsd:sequence>
   		</xsd:sequence>
    </xsd:complexType>
    <xsd:complexType name="CCDType"><!-- Complete Care Diabetes -->
		<xsd:sequence>
   			<xsd:sequence>
    	    <xsd:element name="lastName" type="xsd:string"></xsd:element>
    		<xsd:element name="groupNum" type="xsd:string"></xsd:element>
    		<xsd:element name="dateOfBirth" type="xsd:date"></xsd:element>
    		<xsd:element name="firstName" type="xsd:string"></xsd:element>
    		<xsd:element name="subscriberId" type="xsd:string"></xsd:element>
    		<xsd:element name="individualId" type="xsd:string"></xsd:element>
    		<xsd:element name="zip" type="xsd:string"></xsd:element>
    	</xsd:sequence>
   		</xsd:sequence>
    </xsd:complexType>
    <xsd:complexType name="SubscriberType">
       	<xsd:sequence>
        	<xsd:element name="rowId" type="xsd:int"></xsd:element>
       		<xsd:element name="subscriberid" type="xsd:string"></xsd:element>
    		<xsd:element name="altSubscriberNbr" type="xsd:string"></xsd:element>
			<xsd:element name="systemCode" type="xsd:int"></xsd:element>    		
			<xsd:element name="accountId" type="xsd:int"></xsd:element>    		
			<xsd:element name="groupId" type="xsd:int"></xsd:element>    		
			<xsd:element name="extAccountNbr" type="xsd:string"></xsd:element>
			<xsd:element name="firstName" type="xsd:string"></xsd:element>
    		<xsd:element name="lastName" type="xsd:string"></xsd:element>
    		<xsd:element name="dateOfBirth" type="xsd:date"></xsd:element>
    		<xsd:element name="accessCode" type="xsd:string"></xsd:element>
    		<xsd:element name="submitClaimsFlag" type="xsd:boolean"></xsd:element>    		
    		<xsd:element name="claimsStatusFlag" type="xsd:boolean"></xsd:element>
    		<xsd:element name="clinicianSearchFlag" type="xsd:boolean"></xsd:element> 
    		<xsd:element name="coverageFlag" type="xsd:boolean"></xsd:element> 
    		<xsd:element name="policy" type="xsd:string"></xsd:element> <!-- Added 5/6/2016. Needed for homepage -->
    		<xsd:element name="effectiveDate" type="xsd:date"></xsd:element> <!-- Added 10/17/2016 for GEHUB -->
    		<xsd:element name="expirationDate" type="xsd:date"></xsd:element> <!-- Added 10/17/2016 for GEHUB -->
    	</xsd:sequence>
    </xsd:complexType>
    <xsd:complexType name="RxPlanType">
    <xsd:sequence>
    		<xsd:element name="proxyId" type="xsd:string"></xsd:element><!-- Added on 5/6/2016 -->
    		<xsd:element name="memberId" type="xsd:string"></xsd:element>
    		<xsd:element name="account" type="xsd:string"></xsd:element><!-- Added 10/17/2016 for GEHUB -->
    		<xsd:element name="group" type="xsd:string"></xsd:element><!-- Added 10/17/2016 for GEHUB -->
    		<xsd:element name="carrier" type="xsd:string"></xsd:element><!-- Added 10/17/2016 for GEHUB -->
    		<xsd:element name="effectiveDate" type="xsd:date"></xsd:element><!-- Added 10/17/2016 for GEHUB -->
    		<xsd:element name="expirationDate" type="xsd:date"></xsd:element><!-- Added 10/17/2016 for GEHUB -->
    		</xsd:sequence>
    </xsd:complexType>
	<xsd:simpleType name="RECTYPE" final="restriction">
		<xsd:restriction base="xsd:string">
			<xsd:enumeration value="P" />
			<xsd:enumeration value="F" />
		</xsd:restriction>
	</xsd:simpleType>
</xsd:schema>
