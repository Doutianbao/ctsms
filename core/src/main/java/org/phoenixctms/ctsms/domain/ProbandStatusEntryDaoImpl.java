// Generated by: hibernate/SpringHibernateDaoImpl.vsl in andromda-spring-cartridge.
// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.phoenixctms.ctsms.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.phoenixctms.ctsms.query.CriteriaUtil;
import org.phoenixctms.ctsms.query.SubCriteriaMap;
import org.phoenixctms.ctsms.security.CipherText;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.security.reencrypt.FieldReEncrypter;
import org.phoenixctms.ctsms.security.reencrypt.ReEncrypter;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryInVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandStatusTypeVO;
import org.phoenixctms.ctsms.vo.UserOutVO;

/**
 * @see ProbandStatusEntry
 */
public class ProbandStatusEntryDaoImpl
		extends ProbandStatusEntryDaoBase {

	private final static Collection<ReEncrypter<ProbandStatusEntry>> RE_ENCRYPTERS = new ArrayList<ReEncrypter<ProbandStatusEntry>>();
	static {
		RE_ENCRYPTERS.add(new FieldReEncrypter<ProbandStatusEntry>() {

			@Override
			protected byte[] getIv(ProbandStatusEntry item) {
				return item.getCommentIv();
			}

			@Override
			protected byte[] getEncrypted(ProbandStatusEntry item) {
				return item.getEncryptedComment();
			}

			@Override
			protected void setIv(ProbandStatusEntry item, byte[] iv) {
				item.setCommentIv(iv);
			}

			@Override
			protected void setEncrypted(ProbandStatusEntry item, byte[] cipherText) {
				item.setEncryptedComment(cipherText);
			}

			@Override
			protected void setHash(ProbandStatusEntry item, byte[] hash) {
				item.setCommentHash(hash);
			}
		});
	}

	@Override
	protected Collection<ReEncrypter<ProbandStatusEntry>> getReEncrypters() {
		return RE_ENCRYPTERS;
	}

	private org.hibernate.Criteria createStatusEntryCriteria() {
		org.hibernate.Criteria probandStatusEntryCriteria = this.getSession().createCriteria(ProbandStatusEntry.class);
		return probandStatusEntryCriteria;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	protected Collection<ProbandStatusEntry> handleFindByDepartmentCategoryInterval(Long departmentId, Long probandCategoryId, Timestamp from, Timestamp to, Boolean probandActive,
			Boolean available, Boolean hideAvailability) {
		Criteria statusEntryCriteria = createStatusEntryCriteria();
		CriteriaUtil.applyStopOpenIntervalCriterion(statusEntryCriteria, from, to, null);
		if (probandActive != null || hideAvailability != null) {
			Criteria typeCriteria = statusEntryCriteria.createCriteria("type", CriteriaSpecification.INNER_JOIN);
			if (probandActive != null) {
				typeCriteria.add(Restrictions.eq("probandActive", probandActive.booleanValue()));
			}
			if (hideAvailability != null) {
				typeCriteria.add(Restrictions.eq("hideAvailability", hideAvailability.booleanValue()));
			}
		}
		if (departmentId != null || available != null) {
			Criteria probandCriteria = statusEntryCriteria.createCriteria("proband", CriteriaSpecification.INNER_JOIN);
			if (departmentId != null) {
				probandCriteria.add(Restrictions.eq("department.id", departmentId.longValue()));
			}
			if (probandCategoryId != null) {
				probandCriteria.add(Restrictions.eq("category.id", probandCategoryId.longValue()));
			}
			if (available != null) {
				probandCriteria.add(Restrictions.eq("available", available.booleanValue()));
			}
		}
		return statusEntryCriteria.list();
	}

	/**
	 * @throws Exception
	 * @inheritDoc
	 */
	@Override
	protected Collection<ProbandStatusEntry> handleFindByProband(Long probandId, PSFVO psf) throws Exception {
		Criteria statusEntryCriteria = createStatusEntryCriteria();
		SubCriteriaMap criteriaMap = new SubCriteriaMap(ProbandStatusEntry.class, statusEntryCriteria);
		if (probandId != null) {
			statusEntryCriteria.add(Restrictions.eq("proband.id", probandId.longValue()));
		}
		CriteriaUtil.applyPSFVO(criteriaMap, psf);
		return statusEntryCriteria.list();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	protected Collection<ProbandStatusEntry> handleFindByProbandInterval(Long probandId, Timestamp from, Timestamp to, Boolean probandActive, Boolean hideAvailability) {
		Criteria statusEntryCriteria = createStatusEntryCriteria();
		CriteriaUtil.applyStopOpenIntervalCriterion(statusEntryCriteria, from, to, null);
		if (probandActive != null || hideAvailability != null) {
			Criteria statusTypeCriteria = statusEntryCriteria.createCriteria("type", CriteriaSpecification.INNER_JOIN);
			if (probandActive != null) {
				statusTypeCriteria.add(Restrictions.eq("probandActive", probandActive.booleanValue()));
			}
			if (hideAvailability != null) {
				statusTypeCriteria.add(Restrictions.eq("hideAvailability", hideAvailability.booleanValue()));
			}
		}
		if (probandId != null) {
			statusEntryCriteria.add(Restrictions.eq("proband.id", probandId.longValue()));
		}
		return statusEntryCriteria.list();
	}

	/**
	 * @throws Exception
	 * @inheritDoc
	 */
	@Override
	protected Collection<ProbandStatusEntry> handleFindProbandStatus(Timestamp now, Long probandId, Long departmentId, Long probandCategoryId, Boolean probandActive,
			Boolean hideAvailability, PSFVO psf) throws Exception {
		Criteria statusEntryCriteria = createStatusEntryCriteria();
		SubCriteriaMap criteriaMap = new SubCriteriaMap(ProbandStatusEntry.class, statusEntryCriteria);
		if (probandId != null) {
			statusEntryCriteria.add(Restrictions.eq("proband.id", probandId.longValue()));
		}
		if (departmentId != null) {
			criteriaMap.createCriteria("proband").add(Restrictions.eq("department.id", departmentId.longValue()));
		}
		if (probandCategoryId != null) {
			criteriaMap.createCriteria("proband").add(Restrictions.eq("category.id", probandCategoryId.longValue()));
		}
		if (probandActive != null) {
			criteriaMap.createCriteria("type").add(Restrictions.eq("probandActive", probandActive.booleanValue()));
		}
		if (hideAvailability != null) {
			criteriaMap.createCriteria("type").add(Restrictions.eq("hideAvailability", hideAvailability.booleanValue()));
		}
		CriteriaUtil.applyCurrentStatusCriterion(statusEntryCriteria, now, null);
		CriteriaUtil.applyPSFVO(criteriaMap, psf);
		return statusEntryCriteria.list();
	}

	@Override
	protected long handleGetCount(Long probandId) throws Exception {
		Criteria statusEntryCriteria = createStatusEntryCriteria();
		if (probandId != null) {
			statusEntryCriteria.add(Restrictions.eq("proband.id", probandId.longValue()));
		}
		return (Long) statusEntryCriteria.setProjection(Projections.rowCount()).uniqueResult();
	}

	/**
	 * Retrieves the entity object that is associated with the specified value object
	 * from the object store. If no such entity object exists in the object store,
	 * a new, blank entity is created
	 */
	private ProbandStatusEntry loadProbandStatusEntryFromProbandStatusEntryInVO(ProbandStatusEntryInVO probandStatusEntryInVO) {
		ProbandStatusEntry probandStatusEntry = null;
		Long id = probandStatusEntryInVO.getId();
		if (id != null) {
			probandStatusEntry = this.load(id);
		}
		if (probandStatusEntry == null) {
			probandStatusEntry = ProbandStatusEntry.Factory.newInstance();
		}
		return probandStatusEntry;
	}

	/**
	 * Retrieves the entity object that is associated with the specified value object
	 * from the object store. If no such entity object exists in the object store,
	 * a new, blank entity is created
	 */
	private ProbandStatusEntry loadProbandStatusEntryFromProbandStatusEntryOutVO(ProbandStatusEntryOutVO probandStatusEntryOutVO) {
		ProbandStatusEntry probandStatusEntry = this.load(probandStatusEntryOutVO.getId());
		if (probandStatusEntry == null) {
			probandStatusEntry = ProbandStatusEntry.Factory.newInstance();
		}
		return probandStatusEntry;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public ProbandStatusEntry probandStatusEntryInVOToEntity(ProbandStatusEntryInVO probandStatusEntryInVO) {
		ProbandStatusEntry entity = this.loadProbandStatusEntryFromProbandStatusEntryInVO(probandStatusEntryInVO);
		this.probandStatusEntryInVOToEntity(probandStatusEntryInVO, entity, true);
		return entity;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void probandStatusEntryInVOToEntity(
			ProbandStatusEntryInVO source,
			ProbandStatusEntry target,
			boolean copyIfNull) {
		super.probandStatusEntryInVOToEntity(source, target, copyIfNull);
		Long typeId = source.getTypeId();
		Long probandId = source.getProbandId();
		if (typeId != null) {
			target.setType(this.getProbandStatusTypeDao().load(typeId));
		} else if (copyIfNull) {
			target.setType(null);
		}
		if (probandId != null) {
			Proband proband = this.getProbandDao().load(probandId);
			target.setProband(proband);
			proband.addStatusEntries(target);
		} else if (copyIfNull) {
			Proband proband = target.getProband();
			target.setProband(null);
			if (proband != null) {
				proband.removeStatusEntries(target);
			}
		}
		try {
			if (copyIfNull || source.getComment() != null) {
				CipherText cipherText = CryptoUtil.encryptValue(source.getComment());
				target.setCommentIv(cipherText.getIv());
				target.setEncryptedComment(cipherText.getCipherText());
				target.setCommentHash(CryptoUtil.hashForSearch(source.getComment()));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public ProbandStatusEntry probandStatusEntryOutVOToEntity(ProbandStatusEntryOutVO probandStatusEntryOutVO) {
		ProbandStatusEntry entity = this.loadProbandStatusEntryFromProbandStatusEntryOutVO(probandStatusEntryOutVO);
		this.probandStatusEntryOutVOToEntity(probandStatusEntryOutVO, entity, true);
		return entity;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void probandStatusEntryOutVOToEntity(
			ProbandStatusEntryOutVO source,
			ProbandStatusEntry target,
			boolean copyIfNull) {
		super.probandStatusEntryOutVOToEntity(source, target, copyIfNull);
		ProbandStatusTypeVO typeVO = source.getType();
		ProbandOutVO probandVO = source.getProband();
		UserOutVO modifiedUserVO = source.getModifiedUser();
		if (typeVO != null) {
			target.setType(this.getProbandStatusTypeDao().probandStatusTypeVOToEntity(typeVO));
		} else if (copyIfNull) {
			target.setType(null);
		}
		if (probandVO != null) {
			Proband proband = this.getProbandDao().probandOutVOToEntity(probandVO);
			target.setProband(proband);
			proband.addStatusEntries(target);
		} else if (copyIfNull) {
			Proband proband = target.getProband();
			target.setProband(null);
			if (proband != null) {
				proband.removeStatusEntries(target);
			}
		}
		if (modifiedUserVO != null) {
			target.setModifiedUser(this.getUserDao().userOutVOToEntity(modifiedUserVO));
		} else if (copyIfNull) {
			target.setModifiedUser(null);
		}
		try {
			if (copyIfNull || source.getComment() != null) {
				CipherText cipherText = CryptoUtil.encryptValue(source.getComment());
				target.setCommentIv(cipherText.getIv());
				target.setEncryptedComment(cipherText.getCipherText());
				target.setCommentHash(CryptoUtil.hashForSearch(source.getComment()));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public ProbandStatusEntryInVO toProbandStatusEntryInVO(final ProbandStatusEntry entity) {
		return super.toProbandStatusEntryInVO(entity);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void toProbandStatusEntryInVO(
			ProbandStatusEntry source,
			ProbandStatusEntryInVO target) {
		super.toProbandStatusEntryInVO(source, target);
		ProbandStatusType type = source.getType();
		Proband proband = source.getProband();
		if (type != null) {
			target.setTypeId(type.getId());
		}
		if (proband != null) {
			target.setProbandId(proband.getId());
		}
		try {
			target.setComment((String) CryptoUtil.decryptValue(source.getCommentIv(), source.getEncryptedComment()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public ProbandStatusEntryOutVO toProbandStatusEntryOutVO(final ProbandStatusEntry entity) {
		return super.toProbandStatusEntryOutVO(entity);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void toProbandStatusEntryOutVO(
			ProbandStatusEntry source,
			ProbandStatusEntryOutVO target) {
		super.toProbandStatusEntryOutVO(source, target);
		ProbandStatusType type = source.getType();
		Proband proband = source.getProband();
		User modifiedUser = source.getModifiedUser();
		if (type != null) {
			target.setType(this.getProbandStatusTypeDao().toProbandStatusTypeVO(type));
		}
		if (proband != null) {
			target.setProband(this.getProbandDao().toProbandOutVO(proband));
		}
		if (modifiedUser != null) {
			target.setModifiedUser(this.getUserDao().toUserOutVO(modifiedUser));
		}
		try {
			target.setComment((String) CryptoUtil.decryptValue(source.getCommentIv(), source.getEncryptedComment()));
			target.setDecrypted(true);
		} catch (Exception e) {
			target.setComment(null);
			target.setDecrypted(false);
		}
	}
}