// Generated by: hibernate/SpringHibernateDaoImpl.vsl in andromda-spring-cartridge.
// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.phoenixctms.ctsms.domain;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.PaymentMethod;
import org.phoenixctms.ctsms.query.CategoryCriterion;
import org.phoenixctms.ctsms.query.CategoryCriterion.EmptyPrefixModes;
import org.phoenixctms.ctsms.query.CriteriaUtil;
import org.phoenixctms.ctsms.query.QueryUtil;
import org.phoenixctms.ctsms.query.SubCriteriaMap;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.vo.CriteriaInstantVO;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.SponsoringTypeVO;
import org.phoenixctms.ctsms.vo.SurveyStatusTypeVO;
import org.phoenixctms.ctsms.vo.TrialInVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.TrialStatusTypeVO;
import org.phoenixctms.ctsms.vo.TrialTypeVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vo.VariablePeriodVO;

/**
 * @see Trial
 */
public class TrialDaoImpl
extends TrialDaoBase
{

	private org.hibernate.Criteria createTrialCriteria(String alias) {
		org.hibernate.Criteria trialCriteria;
		if (alias != null && alias.length() > 0) {
			trialCriteria = this.getSession().createCriteria(Trial.class, alias);
		} else {
			trialCriteria = this.getSession().createCriteria(Trial.class);
		}
		return trialCriteria;
	}

	@Override
	protected Collection<Trial> handleFindByCriteria(
			CriteriaInstantVO criteria, PSFVO psf) throws Exception {
		Query query = QueryUtil.createSearchQuery(
				criteria,
				DBModule.TRIAL_DB,
				psf,
				// this.getSession(),
				this.getSessionFactory(),
				this.getCriterionTieDao(),
				this.getCriterionPropertyDao(),
				this.getCriterionRestrictionDao());
		return query.list();
	}

	@Override
	protected Collection<Trial> handleFindByDepartmentPayoffs(Long departmentId, Boolean payoffs, PSFVO psf) throws Exception {
		org.hibernate.Criteria trialCriteria = createTrialCriteria(null);
		SubCriteriaMap criteriaMap = new SubCriteriaMap(Trial.class, trialCriteria);
		if (departmentId != null) {
			trialCriteria.add(Restrictions.eq("department.id", departmentId.longValue()));
		}
		if (payoffs != null) {
			if (payoffs) {
				trialCriteria.add(Restrictions.sizeGt("payoffs", 0));
			} else {
				trialCriteria.add(Restrictions.sizeEq("payoffs", 0));
			}
		}
		CriteriaUtil.applyPSFVO(criteriaMap, psf);
		return trialCriteria.list();
	}

	@Override
	protected Collection<Trial> handleFindByEcrfs(Long departmentId, boolean withChargeOnly, PSFVO psf) throws Exception {
		org.hibernate.Criteria trialCriteria = createTrialCriteria("trial0");
		SubCriteriaMap criteriaMap = new SubCriteriaMap(Trial.class, trialCriteria);
		if (departmentId != null) {
			trialCriteria.add(Restrictions.eq("department.id", departmentId.longValue()));
		}
		DetachedCriteria subQuery = DetachedCriteria.forClass(ECRFImpl.class, "ecrf"); // IMPL!!!!
		subQuery.setProjection(Projections.rowCount());
		subQuery.add(Restrictions.eqProperty("trial.id", "trial0.id"));
		if (withChargeOnly) {
			subQuery.add(Restrictions.gt("charge", 0.0f));
		}
		trialCriteria.add(Subqueries.lt(0l, subQuery));

		CriteriaUtil.applyPSFVO(criteriaMap, psf);
		return trialCriteria.list();
	}

	@Override
	protected Collection<Trial> handleFindByIdDepartment(Long trialId,
			Long departmentId, PSFVO psf) throws Exception {
		org.hibernate.Criteria trialCriteria = createTrialCriteria(null);
		SubCriteriaMap criteriaMap = new SubCriteriaMap(Trial.class, trialCriteria);
		CriteriaUtil.applyIdDepartmentCriterion(trialCriteria, trialId, departmentId);
		CriteriaUtil.applyPSFVO(criteriaMap, psf);
		return trialCriteria.list();
	}

	@Override
	protected Collection<Trial> handleFindByInquiryValuesProbandSorted(Long departmentId, Long probandId, Boolean active, Boolean activeSignup) throws Exception {
		org.hibernate.Criteria trialCriteria = createTrialCriteria("trial0");
		if (departmentId != null) {
			trialCriteria.add(Restrictions.eq("department.id", departmentId.longValue()));
		}
		org.hibernate.Criteria statusCriteria = trialCriteria.createCriteria("status", "trialStatus", CriteriaSpecification.INNER_JOIN);
		DetachedCriteria valuesSubQuery = DetachedCriteria.forClass(InquiryValueImpl.class, "inquiryValue"); // IMPL!!!!
		valuesSubQuery.setProjection(Projections.rowCount());
		valuesSubQuery.add(Restrictions.eq("proband.id", probandId.longValue()));
		// if (active != null || activeSignup != null) {
		DetachedCriteria inquiriesSubQuery = valuesSubQuery.createCriteria("inquiry", "inquiry0", CriteriaSpecification.INNER_JOIN);
		inquiriesSubQuery.add(Restrictions.eqProperty("trial.id", "trial0.id"));
		if (active != null) {
			inquiriesSubQuery.add(Restrictions.eq("active", active.booleanValue()));
		}
		if (activeSignup != null) {
			inquiriesSubQuery.add(Restrictions.eq("activeSignup", activeSignup.booleanValue()));
		}
		inquiriesSubQuery = DetachedCriteria.forClass(InquiryImpl.class, "inquiry1"); // IMPL!!!!
		inquiriesSubQuery.setProjection(Projections.rowCount());
		inquiriesSubQuery.add(Restrictions.eqProperty("trial.id", "trial0.id"));
		if (active != null) {
			inquiriesSubQuery.add(Restrictions.eq("active", active.booleanValue()));
		}
		if (activeSignup != null) {
			inquiriesSubQuery.add(Restrictions.eq("activeSignup", activeSignup.booleanValue()));
		}
		// }
		trialCriteria.add(
				Restrictions.or(
						Subqueries.lt(0l, valuesSubQuery),
						Restrictions.and(
								Subqueries.lt(0l, inquiriesSubQuery),
								Restrictions.and(
										Restrictions.eq("trialStatus.inquiryValueInputEnabled", true),
										Restrictions.eq("trialStatus.lockdown", false)
										)
								)
						)
				);
		trialCriteria.addOrder(Order.asc("name"));
		return trialCriteria.list();
	}

	@Override
	protected Collection<Trial> handleFindByParticipatingProbandSorted(
			Long probandId) throws Exception {
		org.hibernate.Criteria trialCriteria = createTrialCriteria(null);
		trialCriteria.createCriteria("probandListEntries", CriteriaSpecification.INNER_JOIN).add(Restrictions.eq("proband.id", probandId.longValue()));
		trialCriteria.addOrder(Order.asc("name"));
		return trialCriteria.list();
	}

	@Override
	protected Collection<Trial> handleFindByReimbursementProbandSorted(
			Long probandId, PaymentMethod method, String costType, Boolean paid) throws Exception {
		org.hibernate.Criteria trialCriteria = createTrialCriteria(null);
		Criteria payoffCriteria = trialCriteria.createCriteria("payoffs", CriteriaSpecification.INNER_JOIN);
		payoffCriteria.add(Restrictions.eq("proband.id", probandId.longValue()));
		if (method != null) {
			payoffCriteria.add(Restrictions.eq("method", method));
		}
		if (paid != null) {
			payoffCriteria.add(Restrictions.eq("paid", paid.booleanValue()));
		}
		CategoryCriterion.apply(payoffCriteria, new CategoryCriterion(costType, "costType", MatchMode.EXACT, EmptyPrefixModes.ALL_ROWS));
		trialCriteria.addOrder(Order.asc("name"));
		// trialCriteria.setResultTransformer(org.hibernate.Criteria.DISTINCT_ROOT_ENTITY);
		// return trialCriteria.list();
		return CriteriaUtil.listDistinctRoot(trialCriteria, this, "name");
	}

	@Override
	protected Collection<Trial> handleFindBySignup(Long departmentId, boolean ignoreSignupInquiries, PSFVO psf) throws Exception {
		org.hibernate.Criteria trialCriteria = createTrialCriteria("trial0");
		SubCriteriaMap criteriaMap = new SubCriteriaMap(Trial.class, trialCriteria);
		if (departmentId != null) {
			trialCriteria.add(Restrictions.eq("department.id", departmentId.longValue()));
		}
		org.hibernate.Criteria statusCriteria = trialCriteria.createCriteria("status", "trialStatus", CriteriaSpecification.INNER_JOIN);
		statusCriteria.add(Restrictions.eq("lockdown", false));
		DetachedCriteria subQuery = DetachedCriteria.forClass(InquiryImpl.class, "inquiry"); // IMPL!!!!
		subQuery.setProjection(Projections.rowCount());
		subQuery.add(Restrictions.eqProperty("trial.id", "trial0.id"));
		subQuery.add(Restrictions.eq("activeSignup", true));
		trialCriteria.add(
				Restrictions.or(
						Restrictions.eq("signupProbandList", true),
						Restrictions.and(
								ignoreSignupInquiries ? Subqueries.lt(0l, subQuery) : Restrictions.and(
										Restrictions.eq("signupInquiries", true),
										Subqueries.lt(0l, subQuery)
										// Restrictions.sizeGt("inquiries", 0)
										),
										Restrictions.eq("trialStatus.inquiryValueInputEnabled", true)
								)
						)
				);
		// if (probandList != null) {
		// trialCriteria.add(Restrictions.eq("signupProbandList", probandList.booleanValue()));
		// }
		// if (inquiries != null) {
		// trialCriteria.add(Restrictions.eq("signupInquiries", inquiries.booleanValue()));
		// statusCriteria.add(Restrictions.eq("inquiryValueInputEnabled", true));
		// trialCriteria.add(Restrictions.sizeGt("inquiries", 0));
		// }
		CriteriaUtil.applyPSFVO(criteriaMap, psf);
		return trialCriteria.list();
	}

	@Override
	protected long handleGetCountByCriteria(CriteriaInstantVO criteria, PSFVO psf) throws Exception {
		return QueryUtil.getSearchQueryResultCount(
				criteria,
				DBModule.TRIAL_DB,
				psf,
				this.getSessionFactory(),
				this.getCriterionTieDao(),
				this.getCriterionPropertyDao(),
				this.getCriterionRestrictionDao());
	}

	private Trial loadTrialFromTrialInVO(TrialInVO trialInVO)
	{
		Trial trial = null;
		Long id = trialInVO.getId();
		if (id != null) {
			trial = this.load(id);
		}
		if (trial == null)
		{
			trial = Trial.Factory.newInstance();
		}
		return trial;
	}

	/**
	 * Retrieves the entity object that is associated with the specified value object
	 * from the object store. If no such entity object exists in the object store,
	 * a new, blank entity is created
	 */
	private Trial loadTrialFromTrialOutVO(TrialOutVO trialOutVO)
	{
		// TODO implement loadTrialFromTrialOutVO
		// throw new UnsupportedOperationException("org.phoenixctms.ctsms.domain.loadTrialFromTrialOutVO(TrialOutVO) not yet implemented.");
		Trial trial = this.load(trialOutVO.getId());
		if (trial == null)
		{
			trial = Trial.Factory.newInstance();
		}
		return trial;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TrialInVO toTrialInVO(final Trial entity)
	{
		return super.toTrialInVO(entity);
	}

	@Override
	public void toTrialInVO(
			Trial source,
			TrialInVO target)
	{
		super.toTrialInVO(source, target);
		Department department = source.getDepartment();
		TrialStatusType status = source.getStatus();
		TrialType type = source.getType();
		SponsoringType sponsoring = source.getSponsoring();
		SurveyStatusType surveyStatus = source.getSurveyStatus();
		if (department != null) {
			target.setDepartmentId(department.getId());
		}
		if (status != null) {
			target.setStatusId(department.getId());
		}
		if (type != null) {
			target.setTypeId(type.getId());
		}
		if (sponsoring != null) {
			target.setSponsoringId(sponsoring.getId());
		}
		if (surveyStatus != null) {
			target.setSurveyStatusId(surveyStatus.getId());
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public TrialOutVO toTrialOutVO(final Trial entity)
	{
		return super.toTrialOutVO(entity);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void toTrialOutVO(
			Trial source,
			TrialOutVO target)
	{
		super.toTrialOutVO(source, target);
		TrialStatusType status = source.getStatus();
		Department department = source.getDepartment();
		User modifiedUser = source.getModifiedUser();
		TrialType type = source.getType();
		SponsoringType sponsoring = source.getSponsoring();
		SurveyStatusType surveyStatus = source.getSurveyStatus();
		if (status != null) {
			target.setStatus(this.getTrialStatusTypeDao().toTrialStatusTypeVO(status));
		}
		if (department != null) {
			target.setDepartment(this.getDepartmentDao().toDepartmentVO(department));
		}
		if (modifiedUser != null) {
			target.setModifiedUser(this.getUserDao().toUserOutVO(modifiedUser));
		}
		if (type != null) {
			target.setType(this.getTrialTypeDao().toTrialTypeVO(type));
		}
		if (sponsoring != null) {
			target.setSponsoring(this.getSponsoringTypeDao().toSponsoringTypeVO(sponsoring));
		}
		if (surveyStatus != null) {
			target.setSurveyStatus(this.getSurveyStatusTypeDao().toSurveyStatusTypeVO(surveyStatus));
		}
		target.setBlockingPeriod(L10nUtil.createVariablePeriodVO(Locales.USER, source.getBlockingPeriod()));
	}

	@Override
	public Trial trialInVOToEntity(TrialInVO trialInVO) {
		Trial entity = this.loadTrialFromTrialInVO(trialInVO);
		this.trialInVOToEntity(trialInVO, entity, true);
		return entity;
	}

	@Override
	public void trialInVOToEntity(
			TrialInVO source,
			Trial target,
			boolean copyIfNull)
	{
		super.trialInVOToEntity(source, target, copyIfNull);
		Long departmentId = source.getDepartmentId();
		Long statusId = source.getStatusId();
		Long typeId = source.getTypeId();
		Long sponsoringId = source.getSponsoringId();
		Long surveyStatusId = source.getSurveyStatusId();
		if (departmentId != null) {
			target.setDepartment(this.getDepartmentDao().load(departmentId));
		} else if (copyIfNull) {
			target.setDepartment(null);
		}
		if (statusId != null) {
			target.setStatus(this.getTrialStatusTypeDao().load(statusId));
		} else if (copyIfNull) {
			target.setStatus(null);
		}
		if (typeId != null) {
			target.setType(this.getTrialTypeDao().load(typeId));
		} else if (copyIfNull) {
			target.setType(null);
		}
		if (sponsoringId != null) {
			target.setSponsoring(this.getSponsoringTypeDao().load(sponsoringId));
		} else if (copyIfNull) {
			target.setSponsoring(null);
		}
		if (surveyStatusId != null) {
			target.setSurveyStatus(this.getSurveyStatusTypeDao().load(surveyStatusId));
		} else if (copyIfNull) {
			target.setSurveyStatus(null);
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Trial trialOutVOToEntity(TrialOutVO trialOutVO)
	{
		Trial entity = this.loadTrialFromTrialOutVO(trialOutVO);
		this.trialOutVOToEntity(trialOutVO, entity, true);
		return entity;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void trialOutVOToEntity(
			TrialOutVO source,
			Trial target,
			boolean copyIfNull)
	{
		super.trialOutVOToEntity(source, target, copyIfNull);
		TrialStatusTypeVO statusVO = source.getStatus();
		DepartmentVO departmentVO = source.getDepartment();
		UserOutVO modifiedUserVO = source.getModifiedUser();
		TrialTypeVO typeVO = source.getType();
		SponsoringTypeVO sponsoringVO = source.getSponsoring();
		SurveyStatusTypeVO surveyStatusVO = source.getSurveyStatus();
		VariablePeriodVO blockingPeriodVO = source.getBlockingPeriod();
		if (statusVO != null) {
			target.setStatus(this.getTrialStatusTypeDao().trialStatusTypeVOToEntity(statusVO));
		} else if (copyIfNull) {
			target.setStatus(null);
		}
		if (departmentVO != null) {
			target.setDepartment(this.getDepartmentDao().departmentVOToEntity(departmentVO));
		} else if (copyIfNull) {
			target.setDepartment(null);
		}
		if (modifiedUserVO != null) {
			target.setModifiedUser(this.getUserDao().userOutVOToEntity(modifiedUserVO));
		} else if (copyIfNull) {
			target.setModifiedUser(null);
		}
		if (typeVO != null) {
			target.setType(this.getTrialTypeDao().trialTypeVOToEntity(typeVO));
		} else if (copyIfNull) {
			target.setType(null);
		}
		if (sponsoringVO != null) {
			target.setSponsoring(this.getSponsoringTypeDao().sponsoringTypeVOToEntity(sponsoringVO));
		} else if (copyIfNull) {
			target.setSponsoring(null);
		}
		if (surveyStatusVO != null) {
			target.setSurveyStatus(this.getSurveyStatusTypeDao().surveyStatusTypeVOToEntity(surveyStatusVO));
		} else if (copyIfNull) {
			target.setSurveyStatus(null);
		}
		if (blockingPeriodVO != null) {
			target.setBlockingPeriod(blockingPeriodVO.getPeriod());
		} else if (copyIfNull) {
			target.setBlockingPeriod(null);
		}
	}
}