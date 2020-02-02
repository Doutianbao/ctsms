// Generated by: hibernate/SpringHibernateDaoImpl.vsl in andromda-spring-cartridge.
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.phoenixctms.ctsms.domain;

import java.util.Collection;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.phoenixctms.ctsms.query.CategoryCriterion;
import org.phoenixctms.ctsms.query.CriteriaUtil;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.AspAtcCodeVO;

/**
 * @see AspAtcCode
 */
public class AspAtcCodeDaoImpl
		extends AspAtcCodeDaoBase {

	/**
	 * @inheritDoc
	 */
	@Override
	public AspAtcCode aspAtcCodeVOToEntity(AspAtcCodeVO aspAtcCodeVO) {
		AspAtcCode entity = this.loadAspAtcCodeFromAspAtcCodeVO(aspAtcCodeVO);
		this.aspAtcCodeVOToEntity(aspAtcCodeVO, entity, true);
		return entity;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void aspAtcCodeVOToEntity(
			AspAtcCodeVO source,
			AspAtcCode target,
			boolean copyIfNull) {
		super.aspAtcCodeVOToEntity(source, target, copyIfNull);
	}

	@Override
	protected Collection<String> handleFindAspAtcCodeCodes(String codePrefix, Integer limit) throws Exception {
		org.hibernate.Criteria aspAtcCodeCriteria = this.getSession().createCriteria(AspAtcCode.class);
		aspAtcCodeCriteria.setCacheable(true);
		CategoryCriterion.apply(aspAtcCodeCriteria, new CategoryCriterion(codePrefix, "code", MatchMode.START));
		aspAtcCodeCriteria.add(Restrictions.not(Restrictions.or(Restrictions.eq("code", ""), Restrictions.isNull("code"))));
		aspAtcCodeCriteria.add(Restrictions.eq("revision",
				Settings.getString(SettingCodes.ASP_REVISION, Bundle.SETTINGS, DefaultSettings.ASP_REVISION)));
		aspAtcCodeCriteria.addOrder(Order.asc("code"));
		aspAtcCodeCriteria.setProjection(Projections.distinct(Projections.property("code")));
		CriteriaUtil.applyLimit(limit, Settings.getIntNullable(SettingCodes.ASP_ATC_CODE_CODE_AUTOCOMPLETE_DEFAULT_RESULT_LIMIT, Bundle.SETTINGS,
				DefaultSettings.ASP_ATC_CODE_CODE_AUTOCOMPLETE_DEFAULT_RESULT_LIMIT), aspAtcCodeCriteria);
		return aspAtcCodeCriteria.list();
	}

	/**
	 * Retrieves the entity object that is associated with the specified value object
	 * from the object store. If no such entity object exists in the object store,
	 * a new, blank entity is created
	 */
	private AspAtcCode loadAspAtcCodeFromAspAtcCodeVO(AspAtcCodeVO aspAtcCodeVO) {
		AspAtcCode aspAtcCode = this.load(aspAtcCodeVO.getId());
		if (aspAtcCode == null) {
			aspAtcCode = AspAtcCode.Factory.newInstance();
		}
		return aspAtcCode;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public AspAtcCodeVO toAspAtcCodeVO(final AspAtcCode entity) {
		return super.toAspAtcCodeVO(entity);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void toAspAtcCodeVO(
			AspAtcCode source,
			AspAtcCodeVO target) {
		super.toAspAtcCodeVO(source, target);
	}
}