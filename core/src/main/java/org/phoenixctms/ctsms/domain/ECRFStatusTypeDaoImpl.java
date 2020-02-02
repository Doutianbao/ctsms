// Generated by: hibernate/SpringHibernateDaoImpl.vsl in andromda-spring-cartridge.
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.phoenixctms.ctsms.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.phoenixctms.ctsms.compare.EntityIDComparator;
import org.phoenixctms.ctsms.compare.VOIDComparator;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.vo.ECRFStatusActionVO;
import org.phoenixctms.ctsms.vo.ECRFStatusTypeVO;

public class ECRFStatusTypeDaoImpl
		extends ECRFStatusTypeDaoBase {

	private final static EntityIDComparator ID_COMPARATOR = new EntityIDComparator<ECRFStatusType>(false);
	private final static VOIDComparator ACTION_ID_COMPARATOR = new VOIDComparator<ECRFStatusActionVO>(false);

	private org.hibernate.Criteria createEcrfStatusTypeCriteria() {
		org.hibernate.Criteria ecrfStatusTypeCriteria = this.getSession().createCriteria(ECRFStatusType.class);
		ecrfStatusTypeCriteria.setCacheable(true);
		return ecrfStatusTypeCriteria;
	}

	@Override
	public ECRFStatusType eCRFStatusTypeVOToEntity(ECRFStatusTypeVO eCRFStatusTypeVO) {
		ECRFStatusType entity = this.loadECRFStatusTypeFromECRFStatusTypeVO(eCRFStatusTypeVO);
		this.eCRFStatusTypeVOToEntity(eCRFStatusTypeVO, entity, true);
		return entity;
	}

	@Override
	public void eCRFStatusTypeVOToEntity(
			ECRFStatusTypeVO source,
			ECRFStatusType target,
			boolean copyIfNull) {
		super.eCRFStatusTypeVOToEntity(source, target, copyIfNull);
		Collection actions = source.getActions();
		if (actions.size() > 0) {
			actions = new ArrayList(actions); //prevent changing VO
			this.getECRFStatusActionDao().eCRFStatusActionVOToEntityCollection(actions);
			target.setActions(actions);
		} else if (copyIfNull) {
			target.getActions().clear();
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	protected Collection<ECRFStatusType> handleFindInitialStates() {
		org.hibernate.Criteria ecrfStatusTypeCriteria = createEcrfStatusTypeCriteria();
		ecrfStatusTypeCriteria.add(Restrictions.eq("initial", true));
		ecrfStatusTypeCriteria.addOrder(Order.asc("id"));
		return ecrfStatusTypeCriteria.list();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	protected Collection<ECRFStatusType> handleFindTransitions(Long statusTypeId) {
		ECRFStatusType statusType = this.load(statusTypeId);
		Iterator<ECRFStatusType> it = null;
		if (statusType != null) {
			it = statusType.getTransitions().iterator();
		}
		ArrayList<ECRFStatusType> result = new ArrayList<ECRFStatusType>();
		if (it != null) { // force load:
			while (it.hasNext()) {
				result.add(this.load(it.next().getId()));
			}
		}
		Collections.sort(result, ID_COMPARATOR);
		return result;
	}

	/**
	 * Retrieves the entity object that is associated with the specified value object
	 * from the object store. If no such entity object exists in the object store,
	 * a new, blank entity is created
	 */
	private ECRFStatusType loadECRFStatusTypeFromECRFStatusTypeVO(ECRFStatusTypeVO eCRFStatusTypeVO) {
		ECRFStatusType ecrfStatusType = null;
		Long id = eCRFStatusTypeVO.getId();
		if (id != null) {
			ecrfStatusType = this.load(id);
		}
		if (ecrfStatusType == null) {
			ecrfStatusType = ECRFStatusType.Factory.newInstance();
		}
		return ecrfStatusType;
	}

	private ArrayList<ECRFStatusActionVO> toEcrfStatusActionVOCollection(Collection<ECRFStatusAction> actions) {
		// related to http://forum.andromda.org/viewtopic.php?t=4288
		ECRFStatusActionDao ecrfStatusActionDao = this.getECRFStatusActionDao();
		ArrayList<ECRFStatusActionVO> result = new ArrayList<ECRFStatusActionVO>(actions.size());
		Iterator<ECRFStatusAction> it = actions.iterator();
		while (it.hasNext()) {
			result.add(ecrfStatusActionDao.toECRFStatusActionVO(it.next()));
		}
		Collections.sort(result, ACTION_ID_COMPARATOR);
		return result;
	}

	@Override
	public ECRFStatusTypeVO toECRFStatusTypeVO(final ECRFStatusType entity) {
		return super.toECRFStatusTypeVO(entity);
	}

	@Override
	public void toECRFStatusTypeVO(
			ECRFStatusType source,
			ECRFStatusTypeVO target) {
		super.toECRFStatusTypeVO(source, target);
		target.setName(L10nUtil.getEcrfStatusTypeName(Locales.USER, source.getNameL10nKey()));
		target.setActions(toEcrfStatusActionVOCollection(source.getActions()));
	}
}