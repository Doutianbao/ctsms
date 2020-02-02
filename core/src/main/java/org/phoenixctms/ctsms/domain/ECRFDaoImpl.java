// Generated by: hibernate/SpringHibernateDaoImpl.vsl in andromda-spring-cartridge.
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.phoenixctms.ctsms.domain;

import java.text.MessageFormat;
import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.phoenixctms.ctsms.query.CriteriaUtil;
import org.phoenixctms.ctsms.query.SubCriteriaMap;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.ECRFInVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusTypeVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;

public class ECRFDaoImpl
		extends ECRFDaoBase {

	private final static String GROUP_VISIT_TOKEN_SEPARATOR_STRING = ":";
	private static final String UNIQUE_GROUP_VISIT_ECRF_NAME = "{0} {1} - {2}. {3}";
	private static final String UNIQUE_ECRF_POSITION_NAME = "{0} - {1}. {2}";
	private static final String UNIQUE_ECRF_NAME = "{0} - {1}";

	private static String getUniqueInquiryName(ECRFOutVO ecrfVO) {
		if (ecrfVO != null && ecrfVO.getTrial() != null) {
			if (Settings.getBoolean(SettingCodes.UNIQUE_ECRF_NAMES, Bundle.SETTINGS, DefaultSettings.UNIQUE_ECRF_NAMES)) {
				return MessageFormat.format(UNIQUE_ECRF_NAME, ecrfVO.getTrial().getName(), ecrfVO.getName());
			} else {
				VisitOutVO visit = ecrfVO.getVisit();
				ProbandGroupOutVO group = ecrfVO.getGroup();
				if (visit != null || group != null) {
					StringBuilder groupVisit = new StringBuilder();
					if (group != null) {
						groupVisit.append(group.getToken());
					}
					if (visit != null) {
						if (groupVisit.length() > 0) {
							groupVisit.append(GROUP_VISIT_TOKEN_SEPARATOR_STRING);
						}
						groupVisit.append(visit.getToken());
					}
					return MessageFormat
							.format(UNIQUE_GROUP_VISIT_ECRF_NAME, ecrfVO.getTrial().getName(), groupVisit.toString(), Long.toString(ecrfVO.getPosition()), ecrfVO.getName());
				} else {
					return MessageFormat.format(UNIQUE_ECRF_POSITION_NAME, ecrfVO.getTrial().getName(), Long.toString(ecrfVO.getPosition()), ecrfVO.getName());
				}
			}
		}
		return null;
	}

	private org.hibernate.Criteria createEcrfCriteria(String alias) {
		org.hibernate.Criteria ecrfCriteria;
		if (alias != null && alias.length() > 0) {
			ecrfCriteria = this.getSession().createCriteria(ECRF.class, alias);
		} else {
			ecrfCriteria = this.getSession().createCriteria(ECRF.class);
		}
		return ecrfCriteria;
	}

	@Override
	public ECRF eCRFInVOToEntity(ECRFInVO eCRFInVO) {
		ECRF entity = this.loadECRFFromECRFInVO(eCRFInVO);
		this.eCRFInVOToEntity(eCRFInVO, entity, true);
		return entity;
	}

	@Override
	public void eCRFInVOToEntity(
			ECRFInVO source,
			ECRF target,
			boolean copyIfNull) {
		super.eCRFInVOToEntity(source, target, copyIfNull);
		Long trialId = source.getTrialId();
		Long visitId = source.getVisitId();
		Long groupId = source.getGroupId();
		Long probandListStatusId = source.getProbandListStatusId();
		if (trialId != null) {
			Trial trial = this.getTrialDao().load(trialId);
			target.setTrial(trial);
			trial.addEcrfs(target);
		} else if (copyIfNull) {
			Trial trial = target.getTrial();
			target.setTrial(null);
			if (trial != null) {
				trial.removeEcrfs(target);
			}
		}
		if (visitId != null) {
			Visit visit = this.getVisitDao().load(visitId);
			target.setVisit(visit);
			visit.addEcrfs(target);
		} else if (copyIfNull) {
			Visit visit = target.getVisit();
			target.setVisit(null);
			if (visit != null) {
				visit.removeEcrfs(target);
			}
		}
		if (groupId != null) {
			ProbandGroup group = this.getProbandGroupDao().load(groupId);
			target.setGroup(group);
			group.addEcrfs(target);
		} else if (copyIfNull) {
			ProbandGroup group = target.getGroup();
			target.setGroup(null);
			if (group != null) {
				group.removeEcrfs(target);
			}
		}
		if (probandListStatusId != null) {
			target.setProbandListStatus(this.getProbandListStatusTypeDao().load(probandListStatusId));
		} else if (copyIfNull) {
			target.setProbandListStatus(null);
		}
	}

	@Override
	public ECRF eCRFOutVOToEntity(ECRFOutVO eCRFOutVO) {
		ECRF entity = this.loadECRFFromECRFOutVO(eCRFOutVO);
		this.eCRFOutVOToEntity(eCRFOutVO, entity, true);
		return entity;
	}

	@Override
	public void eCRFOutVOToEntity(
			ECRFOutVO source,
			ECRF target,
			boolean copyIfNull) {
		super.eCRFOutVOToEntity(source, target, copyIfNull);
		TrialOutVO trialVO = source.getTrial();
		VisitOutVO visitVO = source.getVisit();
		ProbandGroupOutVO groupVO = source.getGroup();
		ProbandListStatusTypeVO probandListStatusVO = source.getProbandListStatus();
		UserOutVO modifiedUserVO = source.getModifiedUser();
		if (trialVO != null) {
			Trial trial = this.getTrialDao().trialOutVOToEntity(trialVO);
			target.setTrial(trial);
			trial.addEcrfs(target);
		} else if (copyIfNull) {
			Trial trial = target.getTrial();
			target.setTrial(null);
			if (trial != null) {
				trial.removeEcrfs(target);
			}
		}
		if (visitVO != null) {
			Visit visit = this.getVisitDao().visitOutVOToEntity(visitVO);
			target.setVisit(visit);
			visit.addEcrfs(target);
		} else if (copyIfNull) {
			Visit visit = target.getVisit();
			target.setVisit(null);
			if (visit != null) {
				visit.removeEcrfs(target);
			}
		}
		if (groupVO != null) {
			ProbandGroup group = this.getProbandGroupDao().probandGroupOutVOToEntity(groupVO);
			target.setGroup(group);
			group.addEcrfs(target);
		} else if (copyIfNull) {
			ProbandGroup group = target.getGroup();
			target.setGroup(null);
			if (group != null) {
				group.removeEcrfs(target);
			}
		}
		if (probandListStatusVO != null) {
			target.setProbandListStatus(this.getProbandListStatusTypeDao().probandListStatusTypeVOToEntity(probandListStatusVO));
		} else if (copyIfNull) {
			target.setProbandListStatus(null);
		}
		if (modifiedUserVO != null) {
			target.setModifiedUser(this.getUserDao().userOutVOToEntity(modifiedUserVO));
		} else if (copyIfNull) {
			target.setModifiedUser(null);
		}
	}

	@Override
	protected Collection<ECRF> handleFindByListEntryActiveSorted(Long probandListEntryId, Boolean active, boolean sort, PSFVO psf) throws Exception {
		ProbandListEntry listEntry = this.getProbandListEntryDao().load(probandListEntryId);
		Long trialId = listEntry.getTrial().getId();
		Long groupId = (listEntry.getGroup() != null ? listEntry.getGroup().getId() : null);
		org.hibernate.Criteria ecrfCriteria = createEcrfCriteria(null);
		ecrfCriteria.add(Restrictions.eq("trial.id", trialId.longValue()));
		if (groupId != null) {
			// http://stackoverflow.com/questions/4334970/hibernate-cannot-simultaneously-fetch-multiple-bags
			Criteria ecrfFieldCriteria = ecrfCriteria.createCriteria("ecrfFields", CriteriaSpecification.LEFT_JOIN);
			Criteria ecrfFieldValueCriteria = ecrfFieldCriteria.createCriteria("fieldValues", "ecrfFieldValues", CriteriaSpecification.LEFT_JOIN);
			if (active != null) {
				ecrfCriteria.add(Restrictions.or(
						Restrictions.and(
								Restrictions.or(Restrictions.eq("group.id", groupId.longValue()),
										Restrictions.isNull("group.id")),
								Restrictions.eq("active", active.booleanValue())),
						Restrictions.eq("ecrfFieldValues.listEntry.id", probandListEntryId.longValue())));
			} else {
				ecrfCriteria.add(Restrictions.or(
						Restrictions.or(Restrictions.eq("group.id", groupId.longValue()),
								Restrictions.isNull("group.id")),
						Restrictions.eq("ecrfFieldValues.listEntry.id", probandListEntryId.longValue())));
			}
		} else {
			if (active != null) {
				// http://stackoverflow.com/questions/4334970/hibernate-cannot-simultaneously-fetch-multiple-bags
				Criteria ecrfFieldCriteria = ecrfCriteria.createCriteria("ecrfFields", CriteriaSpecification.LEFT_JOIN);
				Criteria ecrfFieldValueCriteria = ecrfFieldCriteria.createCriteria("fieldValues", "ecrfFieldValues", CriteriaSpecification.LEFT_JOIN);
				ecrfCriteria.add(Restrictions.or(
						Restrictions.eq("active", active.booleanValue()),
						Restrictions.eq("ecrfFieldValues.listEntry.id", probandListEntryId.longValue())));
			}
		}
		SubCriteriaMap criteriaMap = new SubCriteriaMap(ECRF.class, ecrfCriteria);
		if (sort) {
			if (psf == null) {
				psf = new PSFVO();
			}
			psf.setSortField("position");
			psf.setSortOrder(true);
		}
		return CriteriaUtil.listDistinctRootPSFVO(criteriaMap, psf, this);
	}

	@Override
	protected Collection<ECRF> handleFindByTrialActiveSorted(Long trialId, Boolean active, boolean sort, PSFVO psf) throws Exception {
		org.hibernate.Criteria ecrfCriteria = createEcrfCriteria(null);
		SubCriteriaMap criteriaMap = new SubCriteriaMap(ECRF.class, ecrfCriteria);
		if (trialId != null) {
			ecrfCriteria.add(Restrictions.eq("trial.id", trialId.longValue()));
		}
		if (active != null) {
			ecrfCriteria.add(Restrictions.eq("active", active.booleanValue()));
		}
		CriteriaUtil.applyPSFVO(criteriaMap, psf);
		if (sort) {
			ecrfCriteria.addOrder(Order.asc("trial"));
			Criteria visitCriteria = ecrfCriteria.createCriteria("visit", CriteriaSpecification.LEFT_JOIN);
			visitCriteria.addOrder(Order.asc("token"));
			Criteria groupCriteria = ecrfCriteria.createCriteria("group", CriteriaSpecification.LEFT_JOIN);
			groupCriteria.addOrder(Order.asc("token"));
			ecrfCriteria.addOrder(Order.asc("position"));
		}
		return ecrfCriteria.list();
	}

	@Override
	protected Collection<ECRF> handleFindByTrialGroupSorted(Long trialId, Long groupId) throws Exception {
		org.hibernate.Criteria ecrfCriteria = createEcrfCriteria(null);
		if (trialId != null) {
			ecrfCriteria.add(Restrictions.eq("trial.id", trialId.longValue()));
		}
		if (groupId != null) {
			ecrfCriteria.add(Restrictions.eq("group.id", groupId.longValue()));
		} else {
			ecrfCriteria.add(Restrictions.isNull("group.id"));
		}
		ecrfCriteria.addOrder(Order.asc("trial"));
		ecrfCriteria.addOrder(Order.asc("position"));
		return ecrfCriteria.list();
	}

	@Override
	protected Collection<ECRF> handleFindByTrialGroupVisitActiveSorted(Long trialId, Long groupId, Long visitId, Boolean active, boolean sort, PSFVO psf) throws Exception {
		org.hibernate.Criteria ecrfCriteria = createEcrfCriteria(null);
		SubCriteriaMap criteriaMap = new SubCriteriaMap(ECRF.class, ecrfCriteria);
		if (trialId != null) {
			ecrfCriteria.add(Restrictions.eq("trial.id", trialId.longValue()));
		}
		if (groupId != null) {
			ecrfCriteria.add(Restrictions.or(Restrictions.eq("group.id", groupId.longValue()),
					Restrictions.isNull("group.id")));
		}
		if (visitId != null) {
			ecrfCriteria.add(Restrictions.or(Restrictions.eq("visit.id", visitId.longValue()),
					Restrictions.isNull("visit.id")));
		}
		if (active != null) {
			ecrfCriteria.add(Restrictions.eq("active", active.booleanValue()));
		}
		CriteriaUtil.applyPSFVO(criteriaMap, psf);
		if (sort) {
			ecrfCriteria.addOrder(Order.asc("trial"));
			ecrfCriteria.addOrder(Order.asc("group"));
			ecrfCriteria.addOrder(Order.asc("position"));
		}
		return ecrfCriteria.list();
	}

	@Override
	protected Collection<ECRF> handleFindByTrialName(Long trialId, String name) throws Exception {
		org.hibernate.Criteria ecrfCriteria = createEcrfCriteria(null);
		if (trialId != null) {
			ecrfCriteria.add(Restrictions.eq("trial.id", trialId.longValue()));
		}
		ecrfCriteria.add(Restrictions.eq("name", name));
		return ecrfCriteria.list();
	}

	@Override
	protected Collection<ECRF> handleFindCollidingTrialGroupPosition(
			Long trialId, Long groupId, Long position) throws Exception {
		org.hibernate.Criteria ecrfCriteria = createEcrfCriteria(null);
		if (trialId != null) {
			ecrfCriteria.add(Restrictions.eq("trial.id", trialId.longValue()));
		}
		if (groupId != null) {
			ecrfCriteria.add(Restrictions.eq("group.id", groupId.longValue()));
		} else {
			ecrfCriteria.add(Restrictions.isNull("group.id"));
		}
		ecrfCriteria.add(Restrictions.eq("position", position.longValue()));
		return ecrfCriteria.list();
	}

	@Override
	protected Long handleFindMaxPosition(Long trialId, Long groupId) throws Exception {
		org.hibernate.Criteria ecrfCriteria = createEcrfCriteria(null);
		if (trialId != null) {
			ecrfCriteria.add(Restrictions.eq("trial.id", trialId.longValue()));
		}
		if (groupId != null) {
			ecrfCriteria.add(Restrictions.eq("group.id", groupId.longValue()));
		} else {
			ecrfCriteria.add(Restrictions.isNull("group.id"));
		}
		ecrfCriteria.setProjection(Projections.max("position"));
		return (Long) ecrfCriteria.uniqueResult();
	}

	@Override
	protected long handleGetCount(Long probandListEntryId, Long visitId, Boolean active, Long ecrfStatusTypeId, Boolean done)
			throws Exception {
		ProbandListEntry listEntry = this.getProbandListEntryDao().load(probandListEntryId);
		Long trialId = listEntry.getTrial().getId();
		Long groupId = (listEntry.getGroup() != null ? listEntry.getGroup().getId() : null);
		org.hibernate.Criteria ecrfCriteria = createEcrfCriteria("ecrf0");
		if (visitId != null) {
			ecrfCriteria.add(Restrictions.eq("visit.id", visitId.longValue()));
		} else {
			ecrfCriteria.add(Restrictions.eq("trial.id", trialId.longValue()));
		}
		if (groupId != null) {
			ecrfCriteria.add(Restrictions.or(Restrictions.eq("group.id", groupId.longValue()),
					Restrictions.isNull("group.id")));
		}
		if (active != null) {
			ecrfCriteria.add(Restrictions.eq("active", active.booleanValue()));
		}
		if (ecrfStatusTypeId != null || done != null) {
			org.hibernate.Criteria ecrfStatusEntryCriteria = ecrfCriteria.createCriteria("ecrfStatusEntries", "statusEntries",
					CriteriaSpecification.LEFT_JOIN,
					Restrictions.and(
							Restrictions.eqProperty("statusEntries.ecrf.id", "ecrf0.id"),
							Restrictions.eq("statusEntries.listEntry.id", probandListEntryId.longValue())));
			org.hibernate.Criteria ecrfStatusTypeCriteria = ecrfStatusEntryCriteria.createCriteria("status", CriteriaSpecification.LEFT_JOIN);
			if (ecrfStatusTypeId != null) {
				ecrfStatusTypeCriteria.add(Restrictions.idEq(ecrfStatusTypeId.longValue()));
			}
			if (done != null) {
				ecrfStatusTypeCriteria.add(Restrictions.eq("done", done.booleanValue()));
			}
		}
		return (Long) ecrfCriteria.setProjection(Projections.rowCount()).uniqueResult();
	}

	@Override
	protected long handleGetCount(Long trialId, Long groupId, Long visitId, Boolean active) throws Exception {
		org.hibernate.Criteria ecrfCriteria = createEcrfCriteria(null);
		if (trialId != null) {
			ecrfCriteria.add(Restrictions.eq("trial.id", trialId.longValue()));
		}
		if (groupId != null) {
			ecrfCriteria.add(Restrictions.or(Restrictions.eq("group.id", groupId.longValue()),
					Restrictions.isNull("group.id")));
		}
		if (visitId != null) {
			ecrfCriteria.add(Restrictions.or(Restrictions.eq("visit.id", visitId.longValue()),
					Restrictions.isNull("visit.id")));
		}
		if (active != null) {
			ecrfCriteria.add(Restrictions.eq("active", active.booleanValue()));
		}
		return (Long) ecrfCriteria.setProjection(Projections.rowCount()).uniqueResult();
	}

	/**
	 * Retrieves the entity object that is associated with the specified value object
	 * from the object store. If no such entity object exists in the object store,
	 * a new, blank entity is created
	 */
	private ECRF loadECRFFromECRFInVO(ECRFInVO eCRFInVO) {
		ECRF ecrf = null;
		Long id = eCRFInVO.getId();
		if (id != null) {
			ecrf = this.load(id);
		}
		if (ecrf == null) {
			ecrf = ECRF.Factory.newInstance();
		}
		return ecrf;
	}

	/**
	 * Retrieves the entity object that is associated with the specified value object
	 * from the object store. If no such entity object exists in the object store,
	 * a new, blank entity is created
	 */
	private ECRF loadECRFFromECRFOutVO(ECRFOutVO eCRFOutVO) {
		ECRF ecrf = this.load(eCRFOutVO.getId());
		if (ecrf == null) {
			ecrf = ECRF.Factory.newInstance();
		}
		return ecrf;
	}

	@Override
	public ECRFInVO toECRFInVO(final ECRF entity) {
		return super.toECRFInVO(entity);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void toECRFInVO(
			ECRF source,
			ECRFInVO target) {
		super.toECRFInVO(source, target);
		Trial trial = source.getTrial();
		Visit visit = source.getVisit();
		ProbandGroup group = source.getGroup();
		ProbandListStatusType probandListStatus = source.getProbandListStatus();
		if (trial != null) {
			target.setTrialId(trial.getId());
		}
		if (visit != null) {
			target.setVisitId(visit.getId());
		}
		if (group != null) {
			target.setGroupId(group.getId());
		}
		if (probandListStatus != null) {
			target.setProbandListStatusId(probandListStatus.getId());
		}
	}

	@Override
	public ECRFOutVO toECRFOutVO(final ECRF entity) {
		return super.toECRFOutVO(entity);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void toECRFOutVO(
			ECRF source,
			ECRFOutVO target) {
		super.toECRFOutVO(source, target);
		Trial trial = source.getTrial();
		Visit visit = source.getVisit();
		ProbandGroup group = source.getGroup();
		ProbandListStatusType probandListStatus = source.getProbandListStatus();
		User modifiedUser = source.getModifiedUser();
		if (trial != null) {
			target.setTrial(this.getTrialDao().toTrialOutVO(trial));
		}
		if (visit != null) {
			target.setVisit(this.getVisitDao().toVisitOutVO(visit));
		}
		if (group != null) {
			target.setGroup(this.getProbandGroupDao().toProbandGroupOutVO(group));
		}
		if (probandListStatus != null) {
			target.setProbandListStatus(this.getProbandListStatusTypeDao().toProbandListStatusTypeVO(probandListStatus));
		}
		if (modifiedUser != null) {
			target.setModifiedUser(this.getUserDao().toUserOutVO(modifiedUser));
		}
		target.setUniqueName(getUniqueInquiryName(target));
	}
}