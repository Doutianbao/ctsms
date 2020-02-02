// Generated by: hibernate/SpringHibernateDaoImpl.vsl in andromda-spring-cartridge.
// license-header java merge-point
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
import org.phoenixctms.ctsms.vo.CountryVO;

/**
 * @see Country
 */
public class CountryDaoImpl
		extends CountryDaoBase {

	/**
	 * @inheritDoc
	 */
	@Override
	public Country countryVOToEntity(CountryVO countryVO) {
		Country entity = this.loadCountryFromCountryVO(countryVO);
		this.countryVOToEntity(countryVO, entity, true);
		return entity;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void countryVOToEntity(
			CountryVO source,
			Country target,
			boolean copyIfNull) {
		super.countryVOToEntity(source, target, copyIfNull);
	}

	private org.hibernate.Criteria createCountryCriteria() {
		org.hibernate.Criteria countryCriteria = this.getSession().createCriteria(Country.class);
		countryCriteria.setCacheable(true);
		return countryCriteria;
	}

	@Override
	protected Collection<CountryVO> handleFindCountries(String countryNameInfix,
			String countryCodeInfix, Integer limit) throws Exception {
		org.hibernate.Criteria countryCriteria = createCountryCriteria();
		CategoryCriterion.applyAnd(countryCriteria,
				new CategoryCriterion(countryNameInfix, "countryName", MatchMode.ANYWHERE),
				new CategoryCriterion(countryCodeInfix, "countryCode", MatchMode.ANYWHERE));
		countryCriteria.addOrder(Order.asc("countryName"));
		CriteriaUtil.applyLimit(limit,
				Settings.getIntNullable(SettingCodes.COUNTRY_AUTOCOMPLETE_DEFAULT_RESULT_LIMIT, Bundle.SETTINGS, DefaultSettings.COUNTRY_AUTOCOMPLETE_DEFAULT_RESULT_LIMIT),
				countryCriteria);
		return countryCriteria.list();
	}

	@Override
	protected Collection<String> handleFindCountryNames(String countryNameInfix, Integer limit)
			throws Exception {
		org.hibernate.Criteria countryCriteria = createCountryCriteria();
		CategoryCriterion.apply(countryCriteria, new CategoryCriterion(countryNameInfix, "countryName", MatchMode.ANYWHERE));
		countryCriteria.add(Restrictions.not(Restrictions.or(Restrictions.eq("countryName", ""), Restrictions.isNull("countryName"))));
		countryCriteria.addOrder(Order.asc("countryName"));
		countryCriteria.setProjection(Projections.distinct(Projections.property("countryName")));
		CriteriaUtil.applyLimit(limit, Settings.getIntNullable(SettingCodes.COUNTRY_NAME_AUTOCOMPLETE_DEFAULT_RESULT_LIMIT, Bundle.SETTINGS,
				DefaultSettings.COUNTRY_NAME_AUTOCOMPLETE_DEFAULT_RESULT_LIMIT), countryCriteria);
		return countryCriteria.list();
	}

	/**
	 * Retrieves the entity object that is associated with the specified value object
	 * from the object store. If no such entity object exists in the object store,
	 * a new, blank entity is created
	 */
	private Country loadCountryFromCountryVO(CountryVO countryVO) {
		Country country = null;
		Long id = countryVO.getId();
		if (id != null) {
			country = this.load(id);
		}
		if (country == null) {
			country = Country.Factory.newInstance();
		}
		return country;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public CountryVO toCountryVO(final Country entity) {
		return super.toCountryVO(entity);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void toCountryVO(
			Country source,
			CountryVO target) {
		super.toCountryVO(source, target);
	}
}