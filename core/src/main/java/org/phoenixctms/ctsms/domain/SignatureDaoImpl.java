// Generated by: hibernate/SpringHibernateDaoImpl.vsl in andromda-spring-cartridge.
// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.phoenixctms.ctsms.domain;

import java.sql.Timestamp;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.phoenixctms.ctsms.enumeration.SignatureModule;
import org.phoenixctms.ctsms.security.EcrfSignature;
import org.phoenixctms.ctsms.security.TrialSignature;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.vo.ECRFStatusEntryVO;
import org.phoenixctms.ctsms.vo.SignatureVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;

/**
 * @see Signature
 */
public class SignatureDaoImpl
		extends SignatureDaoBase {

	@Override
	protected Signature handleAddEcrfSignature(ECRFStatusEntry ecrfStatusEntry, Timestamp now)
			throws Exception {
		Signature signature = Signature.Factory.newInstance();
		signature.setModule(SignatureModule.ECRF_SIGNATURE);
		signature.setSignee(CoreUtil.getUser());
		signature.setTimestamp(now);
		signature.setSignatureData(EcrfSignature.sign(ecrfStatusEntry, now, null, this.getECRFFieldValueDao(), this.getECRFFieldStatusEntryDao()));
		signature.setEcrfStatusEntry(ecrfStatusEntry);
		ecrfStatusEntry.addSignatures(signature);
		return this.create(signature);
	}

	@Override
	protected Signature handleAddTrialSignature(Trial trial, Timestamp now)
			throws Exception {
		Signature signature = Signature.Factory.newInstance();
		signature.setModule(SignatureModule.TRIAL_SIGNATURE);
		signature.setSignee(CoreUtil.getUser());
		signature.setTimestamp(now);
		signature.setSignatureData(TrialSignature.sign(trial, now, null));
		signature.setTrial(trial);
		trial.addSignatures(signature);
		return this.create(signature);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	protected Signature handleFindRecentSignature(SignatureModule module, Long id) {
		org.hibernate.Criteria signatureCriteria = this.getSession().createCriteria(Signature.class);
		if (module != null) {
			signatureCriteria.add(Restrictions.eq("module", module));
			if (id != null) {
				switch (module) {
					case TRIAL_SIGNATURE:
						signatureCriteria.add(Restrictions.eq("trial.id", id.longValue()));
						break;
					case ECRF_SIGNATURE:
						signatureCriteria.add(Restrictions.eq("ecrfStatusEntry.id", id.longValue()));
						break;
					default:
				}
			}
		}
		signatureCriteria.addOrder(Order.desc("id"));
		signatureCriteria.setMaxResults(1);
		return (Signature) signatureCriteria.uniqueResult();
	}

	/**
	 * Retrieves the entity object that is associated with the specified value object
	 * from the object store. If no such entity object exists in the object store,
	 * a new, blank entity is created
	 */
	private Signature loadSignatureFromSignatureVO(SignatureVO signatureVO) {
		Long id = signatureVO.getId();
		Signature signature = null;
		if (id != null) {
			signature = this.load(id);
		}
		if (signature == null) {
			signature = Signature.Factory.newInstance();
		}
		return signature;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Signature signatureVOToEntity(SignatureVO signatureVO) {
		Signature entity = this.loadSignatureFromSignatureVO(signatureVO);
		this.signatureVOToEntity(signatureVO, entity, true);
		return entity;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void signatureVOToEntity(
			SignatureVO source,
			Signature target,
			boolean copyIfNull) {
		super.signatureVOToEntity(source, target, copyIfNull);
		UserOutVO signeeVO = source.getSignee();
		TrialOutVO trialVO = source.getTrial();
		ECRFStatusEntryVO ecrfStatusEntryVO = source.getEcrfStatusEntry();
		String signatureBase64 = source.getSignatureDataBase64();
		if (trialVO != null) {
			Trial trial = this.getTrialDao().trialOutVOToEntity(trialVO);
			target.setTrial(trial);
			trial.addSignatures(target);
		} else if (copyIfNull) {
			Trial trial = target.getTrial();
			target.setTrial(null);
			if (trial != null) {
				trial.removeSignatures(target);
			}
		}
		if (ecrfStatusEntryVO != null) {
			ECRFStatusEntry ecrfStatusEntry = this.getECRFStatusEntryDao().eCRFStatusEntryVOToEntity(ecrfStatusEntryVO);
			target.setEcrfStatusEntry(ecrfStatusEntry);
			ecrfStatusEntry.addSignatures(target);
		} else if (copyIfNull) {
			ECRFStatusEntry ecrfStatusEntry = target.getEcrfStatusEntry();
			target.setEcrfStatusEntry(null);
			if (ecrfStatusEntry != null) {
				ecrfStatusEntry.removeSignatures(target);
			}
		}
		if (signeeVO != null) {
			target.setSignee(this.getUserDao().userOutVOToEntity(signeeVO));
		} else if (copyIfNull) {
			target.setSignee(null);
		}
		if (signatureBase64 != null) {
			target.setSignatureData(Base64.decodeBase64(signatureBase64));
		} else if (copyIfNull) {
			target.setSignatureData(null);
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public SignatureVO toSignatureVO(final Signature entity) {
		return super.toSignatureVO(entity);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void toSignatureVO(
			Signature source,
			SignatureVO target) {
		super.toSignatureVO(source, target);
		User signee = source.getSignee();
		Trial trial = source.getTrial();
		ECRFStatusEntry ecrfStatusEntry = source.getEcrfStatusEntry();
		if (signee != null) {
			target.setSignee(this.getUserDao().toUserOutVO(signee));
		}
		if (trial != null) {
			target.setTrial(this.getTrialDao().toTrialOutVO(trial));
		}
		if (ecrfStatusEntry != null) {
			target.setEcrfStatusEntry(this.getECRFStatusEntryDao().toECRFStatusEntryVO(ecrfStatusEntry));
		}
		target.setSignatureDataBase64(new String(Base64.encodeBase64(source.getSignatureData())));
	}
}