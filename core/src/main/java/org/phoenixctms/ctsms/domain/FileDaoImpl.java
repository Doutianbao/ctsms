// Generated by: hibernate/SpringHibernateDaoImpl.vsl in andromda-spring-cartridge.
// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.phoenixctms.ctsms.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import org.hibernate.Hibernate;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.phoenixctms.ctsms.compare.AlphanumStringComparator;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.query.CriteriaUtil;
import org.phoenixctms.ctsms.query.SubCriteriaMap;
import org.phoenixctms.ctsms.security.CipherText;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.security.VerifyMD5InputStream;
import org.phoenixctms.ctsms.security.reencrypt.DataReEncrypter;
import org.phoenixctms.ctsms.security.reencrypt.FieldReEncrypter;
import org.phoenixctms.ctsms.security.reencrypt.FileReEncrypter;
import org.phoenixctms.ctsms.security.reencrypt.ReEncrypter;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.FileContentOutVO;
import org.phoenixctms.ctsms.vo.FileInVO;
import org.phoenixctms.ctsms.vo.FileOutVO;
import org.phoenixctms.ctsms.vo.FileStreamOutVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.MassMailOutVO;
import org.phoenixctms.ctsms.vo.MimeTypeVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;

/**
 * @see File
 */
public class FileDaoImpl
		extends FileDaoBase {

	private final static void applyContentTypeCriterions(org.hibernate.Criteria criteria, Boolean image, String mimeType) {
		if (image != null || (mimeType != null && mimeType.length() > 0)) {
			org.hibernate.Criteria contentTypeCriteria = criteria.createCriteria("contentType");
			if (image != null) {
				contentTypeCriteria.add(Restrictions.eq("image", image.booleanValue()));
			}
			if (mimeType != null && mimeType.length() > 0) {
				contentTypeCriteria.add(Restrictions.eq("mimeType", mimeType));
			}
		}
	}

	private final static void applyModuleIdCriterions(org.hibernate.Criteria criteria, FileModule module, Long id) {
		if (module != null) {
			criteria.add(Restrictions.eq("module", module));
			if (id != null) {
				switch (module) {
					case INVENTORY_DOCUMENT:
						criteria.add(Restrictions.eq("inventory.id", id.longValue()));
						break;
					case STAFF_DOCUMENT:
						criteria.add(Restrictions.eq("staff.id", id.longValue()));
						break;
					case COURSE_DOCUMENT:
						criteria.add(Restrictions.eq("course.id", id.longValue()));
						break;
					case TRIAL_DOCUMENT:
						criteria.add(Restrictions.eq("trial.id", id.longValue()));
						break;
					case PROBAND_DOCUMENT:
						criteria.add(Restrictions.eq("proband.id", id.longValue()));
						break;
					case MASS_MAIL_DOCUMENT:
						criteria.add(Restrictions.eq("massMail.id", id.longValue()));
						break;
					default:
				}
			}
		}
	}

	private final static void applySubTreeCriterion(org.hibernate.Criteria criteria, boolean subTree, String logicalPath) {
		if (logicalPath != null && logicalPath.length() > 0) {
			logicalPath = CommonUtil.fixLogicalPathFolderName(logicalPath);
			if (subTree) {
				criteria.add(Restrictions.sqlRestriction("substr({alias}.logical_path, 1, length(?)) = ?",
						new Object[] { logicalPath, logicalPath },
						new org.hibernate.type.NullableType[] { Hibernate.STRING, Hibernate.STRING }));
			} else {
				criteria.add(Restrictions.eq("logicalPath", logicalPath));
			}
		}
	}

	private final static Collection<ReEncrypter<File>> RE_ENCRYPTERS = new ArrayList<ReEncrypter<File>>();
	static {
		RE_ENCRYPTERS.add(new FileReEncrypter<File>() {

			@Override
			protected String getMd5(File item) {
				return item.getMd5();
			}

			@Override
			protected String getExternalFileName(File item) {
				return item.getExternalFileName();
			}

			@Override
			protected void setExternalFileName(File item, String externalFileName) {
				item.setExternalFileName(externalFileName);
			}

			@Override
			protected byte[] getIv(File item) {
				return item.getDataIv();
			}

			@Override
			protected void setIv(File item, byte[] iv) {
				item.setDataIv(iv);
			}

			@Override
			protected boolean isSkip(File item) {
				return !(item.isExternalFile() && CommonUtil.getUseFileEncryption(item.getModule()));
			}

			@Override
			protected FileModule getModule(File item) {
				return item.getModule();
			}
		});
		RE_ENCRYPTERS.add(new DataReEncrypter<File>() {

			@Override
			protected byte[] getIv(File item) {
				return item.getDataIv();
			}

			@Override
			protected byte[] getEncrypted(File item) {
				return item.getData();
			}

			@Override
			protected void setIv(File item, byte[] iv) {
				item.setDataIv(iv);
			}

			@Override
			protected void setEncrypted(File item, byte[] cipherText) {
				item.setData(cipherText);
			}

			@Override
			protected boolean isSkip(File item) {
				return !(!item.isExternalFile() && CommonUtil.getUseFileEncryption(item.getModule()));
			}
		});
		RE_ENCRYPTERS.add(new FieldReEncrypter<File>() {

			@Override
			protected byte[] getIv(File item) {
				return item.getFileNameIv();
			}

			@Override
			protected byte[] getEncrypted(File item) {
				return item.getEncryptedFileName();
			}

			@Override
			protected void setIv(File item, byte[] iv) {
				item.setFileNameIv(iv);
			}

			@Override
			protected void setEncrypted(File item, byte[] cipherText) {
				item.setEncryptedFileName(cipherText);
			}

			@Override
			protected void setHash(File item, byte[] hash) {
				item.setFileNameHash(hash);
			}

			@Override
			protected boolean isSkip(File item) {
				return !CommonUtil.getUseFileEncryption(item.getModule());
			}
		});
		RE_ENCRYPTERS.add(new FieldReEncrypter<File>() {

			@Override
			protected byte[] getIv(File item) {
				return item.getTitleIv();
			}

			@Override
			protected byte[] getEncrypted(File item) {
				return item.getEncryptedTitle();
			}

			@Override
			protected void setIv(File item, byte[] iv) {
				item.setTitleIv(iv);
			}

			@Override
			protected void setEncrypted(File item, byte[] cipherText) {
				item.setEncryptedTitle(cipherText);
			}

			@Override
			protected void setHash(File item, byte[] hash) {
				item.setTitleHash(hash);
			}

			@Override
			protected boolean isSkip(File item) {
				return !CommonUtil.getUseFileEncryption(item.getModule());
			}
		});
		RE_ENCRYPTERS.add(new FieldReEncrypter<File>() {

			@Override
			protected byte[] getIv(File item) {
				return item.getCommentIv();
			}

			@Override
			protected byte[] getEncrypted(File item) {
				return item.getEncryptedComment();
			}

			@Override
			protected void setIv(File item, byte[] iv) {
				item.setCommentIv(iv);
			}

			@Override
			protected void setEncrypted(File item, byte[] cipherText) {
				item.setEncryptedComment(cipherText);
			}

			@Override
			protected void setHash(File item, byte[] hash) {
				item.setCommentHash(hash);
			}

			@Override
			protected boolean isSkip(File item) {
				return !CommonUtil.getUseFileEncryption(item.getModule());
			}
		});
	}

	@Override
	protected Collection<ReEncrypter<File>> getReEncrypters() {
		return RE_ENCRYPTERS;
	}

	private org.hibernate.Criteria createFileCriteria() {
		org.hibernate.Criteria fileCriteria = this.getSession().createCriteria(File.class);
		return fileCriteria;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public File fileContentOutVOToEntity(FileContentOutVO fileContentOutVO) {
		File entity = this.loadFileFromFileContentOutVO(fileContentOutVO);
		this.fileContentOutVOToEntity(fileContentOutVO, entity, true);
		return entity;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void fileContentOutVOToEntity(
			FileContentOutVO source,
			File target,
			boolean copyIfNull) {
		super.fileContentOutVOToEntity(source, target, copyIfNull);
		MimeTypeVO contentTypeVO = source.getContentType();
		UserOutVO modifiedUserVO = source.getModifiedUser();
		if (contentTypeVO != null) {
			target.setContentType(this.getMimeTypeDao().mimeTypeVOToEntity(contentTypeVO));
		} else if (copyIfNull) {
			target.setContentType(null);
		}
		if (modifiedUserVO != null) {
			target.setModifiedUser(this.getUserDao().userOutVOToEntity(modifiedUserVO));
		} else if (copyIfNull) {
			target.setModifiedUser(null);
		}
		if (CommonUtil.getUseFileEncryption(target.getModule())) {
			try {
				if (copyIfNull || source.getDatas() != null) {
					CipherText cipherText = CryptoUtil.encrypt(source.getDatas());
					target.setDataIv(cipherText.getIv());
					target.setData(cipherText.getCipherText());
				}
				if (copyIfNull || source.getFileName() != null) {
					CipherText cipherText = CryptoUtil.encryptValue(source.getFileName());
					target.setFileNameIv(cipherText.getIv());
					target.setEncryptedFileName(cipherText.getCipherText());
					target.setFileNameHash(CryptoUtil.hashForSearch(source.getFileName()));
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				target.setFileName(null);
			}
		} else {
			if (copyIfNull || source.getDatas() != null) {
				target.setData(source.getDatas());
			}
			target.setFileNameIv(null);
			target.setEncryptedFileName(null);
			target.setFileNameHash(null);
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public File fileInVOToEntity(FileInVO fileInVO) {
		File entity = this.loadFileFromFileInVO(fileInVO);
		this.fileInVOToEntity(fileInVO, entity, true);
		return entity;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void fileInVOToEntity(
			FileInVO source,
			File target,
			boolean copyIfNull) {
		super.fileInVOToEntity(source, target, copyIfNull);
		Long inventoryId = source.getInventoryId();
		Long staffId = source.getStaffId();
		Long courseId = source.getCourseId();
		Long trialId = source.getTrialId();
		Long probandId = source.getProbandId();
		Long massMailId = source.getMassMailId();
		if (inventoryId != null) {
			Inventory inventory = this.getInventoryDao().load(inventoryId);
			target.setInventory(inventory);
			inventory.addFiles(target);
		} else if (copyIfNull) {
			Inventory inventory = target.getInventory();
			target.setInventory(null);
			if (inventory != null) {
				inventory.removeFiles(target);
			}
		}
		if (staffId != null) {
			Staff staff = this.getStaffDao().load(staffId);
			target.setStaff(staff);
			staff.addFiles(target);
		} else if (copyIfNull) {
			Staff staff = target.getStaff();
			target.setStaff(null);
			if (staff != null) {
				staff.removeFiles(target);
			}
		}
		if (courseId != null) {
			Course course = this.getCourseDao().load(courseId);
			target.setCourse(course);
			course.addFiles(target);
		} else if (copyIfNull) {
			Course course = target.getCourse();
			target.setCourse(null);
			if (course != null) {
				course.removeFiles(target);
			}
		}
		if (trialId != null) {
			Trial trial = this.getTrialDao().load(trialId);
			target.setTrial(trial);
			trial.addFiles(target);
		} else if (copyIfNull) {
			Trial trial = target.getTrial();
			target.setTrial(null);
			if (trial != null) {
				trial.removeFiles(target);
			}
		}
		if (probandId != null) {
			Proband proband = this.getProbandDao().load(probandId);
			target.setProband(proband);
			proband.addFiles(target);
		} else if (copyIfNull) {
			Proband proband = target.getProband();
			target.setProband(null);
			if (proband != null) {
				proband.removeFiles(target);
			}
		}
		if (massMailId != null) {
			MassMail massMail = this.getMassMailDao().load(massMailId);
			target.setMassMail(massMail);
			massMail.addFiles(target);
		} else if (copyIfNull) {
			MassMail massMail = target.getMassMail();
			target.setMassMail(null);
			if (massMail != null) {
				massMail.removeFiles(target);
			}
		}
		if (CommonUtil.getUseFileEncryption(source.getModule())) {
			try {
				if (copyIfNull || source.getTitle() != null) {
					CipherText cipherText = CryptoUtil.encryptValue(source.getTitle());
					target.setTitleIv(cipherText.getIv());
					target.setEncryptedTitle(cipherText.getCipherText());
					target.setTitleHash(CryptoUtil.hashForSearch(source.getTitle()));
				}
				if (copyIfNull || source.getComment() != null) {
					CipherText cipherText = CryptoUtil.encryptValue(source.getComment());
					target.setCommentIv(cipherText.getIv());
					target.setEncryptedComment(cipherText.getCipherText());
					target.setCommentHash(CryptoUtil.hashForSearch(source.getComment()));
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				target.setTitle(null);
				target.setComment(null);
			}
		} else {
			target.setTitleIv(null);
			target.setEncryptedTitle(null);
			target.setTitleHash(null);
			target.setCommentIv(null);
			target.setEncryptedComment(null);
			target.setCommentHash(null);
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public File fileOutVOToEntity(FileOutVO fileOutVO) {
		File entity = this.loadFileFromFileOutVO(fileOutVO);
		this.fileOutVOToEntity(fileOutVO, entity, true);
		return entity;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void fileOutVOToEntity(
			FileOutVO source,
			File target,
			boolean copyIfNull) {
		super.fileOutVOToEntity(source, target, copyIfNull);
		MimeTypeVO contentTypeVO = source.getContentType();
		InventoryOutVO inventoryVO = source.getInventory();
		StaffOutVO staffVO = source.getStaff();
		CourseOutVO courseVO = source.getCourse();
		TrialOutVO trialVO = source.getTrial();
		ProbandOutVO probandVO = source.getProband();
		UserOutVO modifiedUserVO = source.getModifiedUser();
		MassMailOutVO massMailVO = source.getMassMail();
		if (contentTypeVO != null) {
			target.setContentType(this.getMimeTypeDao().mimeTypeVOToEntity(contentTypeVO));
		} else if (copyIfNull) {
			target.setContentType(null);
		}
		if (inventoryVO != null) {
			Inventory inventory = this.getInventoryDao().inventoryOutVOToEntity(inventoryVO);
			target.setInventory(inventory);
			inventory.addFiles(target);
		} else if (copyIfNull) {
			Inventory inventory = target.getInventory();
			target.setInventory(null);
			if (inventory != null) {
				inventory.removeFiles(target);
			}
		}
		if (staffVO != null) {
			Staff staff = this.getStaffDao().staffOutVOToEntity(staffVO);
			target.setStaff(staff);
			staff.addFiles(target);
		} else if (copyIfNull) {
			Staff staff = target.getStaff();
			target.setStaff(null);
			if (staff != null) {
				staff.removeFiles(target);
			}
		}
		if (courseVO != null) {
			Course course = this.getCourseDao().courseOutVOToEntity(courseVO);
			target.setCourse(course);
			course.addFiles(target);
		} else if (copyIfNull) {
			Course course = target.getCourse();
			target.setCourse(null);
			if (course != null) {
				course.removeFiles(target);
			}
		}
		if (trialVO != null) {
			Trial trial = this.getTrialDao().trialOutVOToEntity(trialVO);
			target.setTrial(trial);
			trial.addFiles(target);
		} else if (copyIfNull) {
			Trial trial = target.getTrial();
			target.setTrial(null);
			if (trial != null) {
				trial.removeFiles(target);
			}
		}
		if (probandVO != null) {
			Proband proband = this.getProbandDao().probandOutVOToEntity(probandVO);
			target.setProband(proband);
			proband.addFiles(target);
		} else if (copyIfNull) {
			Proband proband = target.getProband();
			target.setProband(null);
			if (proband != null) {
				proband.removeFiles(target);
			}
		}
		if (massMailVO != null) {
			MassMail massMail = this.getMassMailDao().massMailOutVOToEntity(massMailVO);
			target.setMassMail(massMail);
			massMail.addFiles(target);
		} else if (copyIfNull) {
			MassMail massMail = target.getMassMail();
			target.setMassMail(null);
			if (massMail != null) {
				massMail.removeFiles(target);
			}
		}
		if (modifiedUserVO != null) {
			target.setModifiedUser(this.getUserDao().userOutVOToEntity(modifiedUserVO));
		} else if (copyIfNull) {
			target.setModifiedUser(null);
		}
		if (CommonUtil.getUseFileEncryption(source.getModule())) {
			try {
				if (copyIfNull || source.getTitle() != null) {
					CipherText cipherText = CryptoUtil.encryptValue(source.getTitle());
					target.setTitleIv(cipherText.getIv());
					target.setEncryptedTitle(cipherText.getCipherText());
					target.setTitleHash(CryptoUtil.hashForSearch(source.getTitle()));
				}
				if (copyIfNull || source.getComment() != null) {
					CipherText cipherText = CryptoUtil.encryptValue(source.getComment());
					target.setCommentIv(cipherText.getIv());
					target.setEncryptedComment(cipherText.getCipherText());
					target.setCommentHash(CryptoUtil.hashForSearch(source.getComment()));
				}
				if (copyIfNull || source.getFileName() != null) {
					CipherText cipherText = CryptoUtil.encryptValue(source.getFileName());
					target.setFileNameIv(cipherText.getIv());
					target.setEncryptedFileName(cipherText.getCipherText());
					target.setFileNameHash(CryptoUtil.hashForSearch(source.getFileName()));
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				target.setTitle(null);
				target.setComment(null);
				target.setFileName(null);
			}
		} else {
			target.setTitleIv(null);
			target.setEncryptedTitle(null);
			target.setTitleHash(null);
			target.setCommentIv(null);
			target.setEncryptedComment(null);
			target.setCommentHash(null);
			target.setFileNameIv(null);
			target.setEncryptedFileName(null);
			target.setFileNameHash(null);
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public File fileStreamOutVOToEntity(FileStreamOutVO fileStreamOutVO) {
		File entity = this.loadFileFromFileStreamOutVO(fileStreamOutVO);
		this.fileStreamOutVOToEntity(fileStreamOutVO, entity, true);
		return entity;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void fileStreamOutVOToEntity(
			FileStreamOutVO source,
			File target,
			boolean copyIfNull) {
		super.fileStreamOutVOToEntity(source, target, copyIfNull);
		MimeTypeVO contentTypeVO = source.getContentType();
		UserOutVO modifiedUserVO = source.getModifiedUser();
		if (contentTypeVO != null) {
			target.setContentType(this.getMimeTypeDao().mimeTypeVOToEntity(contentTypeVO));
		} else if (copyIfNull) {
			target.setContentType(null);
		}
		if (modifiedUserVO != null) {
			target.setModifiedUser(this.getUserDao().userOutVOToEntity(modifiedUserVO));
		} else if (copyIfNull) {
			target.setModifiedUser(null);
		}
		if (CommonUtil.getUseFileEncryption(target.getModule())) {
			try {
				if (source.getStream() != null) {
					CipherText cipherText = CryptoUtil.encrypt(CommonUtil.inputStreamToByteArray(source.getStream()));
					target.setDataIv(cipherText.getIv());
					target.setData(cipherText.getCipherText());
				} else if (copyIfNull) {
					target.setData(null);
				}
				if (copyIfNull || source.getFileName() != null) {
					CipherText cipherText = CryptoUtil.encryptValue(source.getFileName());
					target.setFileNameIv(cipherText.getIv());
					target.setEncryptedFileName(cipherText.getCipherText());
					target.setFileNameHash(CryptoUtil.hashForSearch(source.getFileName()));
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				target.setFileName(null);
			}
		} else {
			if (source.getStream() != null) {
				try {
					target.setData(CommonUtil.inputStreamToByteArray(source.getStream()));
				} catch (Exception e) {
				}
			} else if (copyIfNull) {
				target.setData(null);
			}
			target.setFileNameIv(null);
			target.setEncryptedFileName(null);
			target.setFileNameHash(null);
		}
	}

	@Override
	protected Collection<File> handleFindByExternalFileName(
			String externalFileName) throws Exception {
		org.hibernate.Criteria fileCriteria = createFileCriteria();
		fileCriteria.add(Restrictions.eq("externalFile", true));
		fileCriteria.add(Restrictions.eq("externalFileName", externalFileName));
		return fileCriteria.list();
	}

	@Override
	protected Collection<String> handleFindFileFolders(FileModule module,
			Long id, String parentLogicalPath, boolean complete, Boolean active, Boolean publicFile, Boolean image, PSFVO psf) throws Exception {
		org.hibernate.Criteria fileFolderPresetCriteria = this.getSession().createCriteria(FileFolderPreset.class);
		fileFolderPresetCriteria.setCacheable(true);
		boolean useParentPath;
		ArrayList<String> result = new ArrayList<String>();
		HashSet<String> dupeCheck = new HashSet<String>(result);
		String parentLogicalFolder = CommonUtil.fixLogicalPathFolderName(parentLogicalPath, !complete);
		int depth;
		PSFVO f;
		if (psf != null) {
			f = new PSFVO();
			f.setFilters(psf.getFilters());
		} else {
			f = null;
		}
		fileFolderPresetCriteria.add(Restrictions.eq("module", module));
		if (parentLogicalPath != null && parentLogicalPath.length() > 0) {
			fileFolderPresetCriteria.add(Restrictions.like("logicalPath", parentLogicalFolder, MatchMode.START));
			depth = CommonUtil.getLogicalPathFolderDepth(parentLogicalFolder);
			useParentPath = true;
		} else {
			depth = 0;
			useParentPath = false;
		}
		Integer limit = complete ? Settings.getIntNullable(SettingCodes.FILE_FOLDER_AUTOCOMPLETE_DEFAULT_RESULT_LIMIT, Bundle.SETTINGS,
				DefaultSettings.FILE_FOLDER_AUTOCOMPLETE_DEFAULT_RESULT_LIMIT) : null;
		if (f == null || f.getFilters() == null || f.getFilters().size() == 0) {
			fileFolderPresetCriteria.setProjection(Projections.distinct(Projections.property("logicalPath")));
			Iterator<String> folderPresetIt = fileFolderPresetCriteria.list().iterator();
			while (folderPresetIt.hasNext()) {
				if (limit != null && limit >= 0 && result.size() >= limit) {
					break;
				}
				String folder = folderPresetIt.next();
				if (CommonUtil.getLogicalPathFolderDepth(folder) > depth) {
					folder = CommonUtil.getLogicalPathFolderToDepth(folder, depth + 1);
					if (dupeCheck.add(folder)) {
						result.add(folder);
					}
				}
			}
		}
		if (id != null) {
			org.hibernate.Criteria fileCriteria = createFileCriteria();
			SubCriteriaMap criteriaMap = new SubCriteriaMap(File.class, fileCriteria);
			applyModuleIdCriterions(fileCriteria, module, id);
			if (useParentPath) {
				fileCriteria.add(Restrictions.like("logicalPath", parentLogicalFolder, MatchMode.START));
			}
			if (active != null) {
				fileCriteria.add(Restrictions.eq("active", active.booleanValue()));
			}
			if (publicFile != null) {
				fileCriteria.add(Restrictions.eq("publicFile", publicFile.booleanValue()));
			}
			applyContentTypeCriterions(fileCriteria, image, null);
			CriteriaUtil.applyPSFVO(criteriaMap, f);
			fileCriteria.setProjection(Projections.distinct(Projections.property("logicalPath")));
			Iterator<String> fileFolderIt = fileCriteria.list().iterator();
			while (fileFolderIt.hasNext()) {
				if (limit != null && limit >= 0 && result.size() >= limit) {
					break;
				}
				String folder = fileFolderIt.next();
				if (CommonUtil.getLogicalPathFolderDepth(folder) > depth) {
					folder = CommonUtil.getLogicalPathFolderToDepth(folder, depth + 1);
					if (dupeCheck.add(folder)) {
						result.add(folder);
					}
				}
			}
		}
		Collections.sort(result, new AlphanumStringComparator(false));
		return result;
	}

	@Override
	protected Collection<File> handleFindFiles(FileModule module, Long id, String logicalPath, boolean subTree,
			Boolean active, Boolean publicFile, Boolean image, String mimeType, PSFVO psf) throws Exception {
		org.hibernate.Criteria fileCriteria = createFileCriteria();
		SubCriteriaMap criteriaMap = new SubCriteriaMap(File.class, fileCriteria);
		applyModuleIdCriterions(fileCriteria, module, id);
		applySubTreeCriterion(fileCriteria, subTree, logicalPath);
		if (active != null) {
			fileCriteria.add(Restrictions.eq("active", active.booleanValue()));
		}
		if (publicFile != null) {
			fileCriteria.add(Restrictions.eq("publicFile", publicFile.booleanValue()));
		}
		applyContentTypeCriterions(fileCriteria, image, mimeType);
		CriteriaUtil.applyPSFVO(criteriaMap, psf);
		return fileCriteria.list();
	}

	@Override
	protected long handleGetCount(FileModule module, Long id, String logicalPath, boolean subTree,
			Boolean active, Boolean publicFile, Boolean image, String mimeType) throws Exception {
		org.hibernate.Criteria fileCriteria = createFileCriteria();
		applyModuleIdCriterions(fileCriteria, module, id);
		applySubTreeCriterion(fileCriteria, subTree, logicalPath);
		if (active != null) {
			fileCriteria.add(Restrictions.eq("active", active.booleanValue()));
		}
		if (publicFile != null) {
			fileCriteria.add(Restrictions.eq("publicFile", publicFile.booleanValue()));
		}
		applyContentTypeCriterions(fileCriteria, image, mimeType);
		return (Long) fileCriteria.setProjection(Projections.rowCount()).uniqueResult();
	}

	@Override
	protected long handleGetCount(
			String externalFileName) throws Exception {
		org.hibernate.Criteria fileCriteria = createFileCriteria();
		fileCriteria.add(Restrictions.eq("externalFile", true));
		fileCriteria.add(Restrictions.eq("externalFileName", externalFileName));
		return (Long) fileCriteria.setProjection(Projections.rowCount()).uniqueResult();
	}

	@Override
	protected long handleGetFileSizeSum(FileModule module, Long id, String logicalPath, boolean subTree,
			Boolean active, Boolean publicFile, Boolean image, String mimeType) throws Exception {
		org.hibernate.Criteria fileCriteria = createFileCriteria();
		applyModuleIdCriterions(fileCriteria, module, id);
		applySubTreeCriterion(fileCriteria, subTree, logicalPath);
		if (active != null) {
			fileCriteria.add(Restrictions.eq("active", active.booleanValue()));
		}
		if (publicFile != null) {
			fileCriteria.add(Restrictions.eq("publicFile", publicFile.booleanValue()));
		}
		applyContentTypeCriterions(fileCriteria, image, mimeType);
		try {
			return (Long) fileCriteria.setProjection(Projections.sum("size")).uniqueResult();
		} catch (NullPointerException e) { // sum() returns NULL if no records, so reduce log noise
			return 0l;
		}
	}

	/**
	 * Retrieves the entity object that is associated with the specified value object
	 * from the object store. If no such entity object exists in the object store,
	 * a new, blank entity is created
	 */
	private File loadFileFromFileContentOutVO(FileContentOutVO fileContentOutVO) {
		File file = this.load(fileContentOutVO.getId());
		if (file == null) {
			file = File.Factory.newInstance();
		}
		return file;
	}

	/**
	 * Retrieves the entity object that is associated with the specified value object
	 * from the object store. If no such entity object exists in the object store,
	 * a new, blank entity is created
	 */
	private File loadFileFromFileInVO(FileInVO fileInVO) {
		File file = null;
		Long id = fileInVO.getId();
		if (id != null) {
			file = this.load(id);
		}
		if (file == null) {
			file = File.Factory.newInstance();
		}
		return file;
	}

	/**
	 * Retrieves the entity object that is associated with the specified value object
	 * from the object store. If no such entity object exists in the object store,
	 * a new, blank entity is created
	 */
	private File loadFileFromFileOutVO(FileOutVO fileOutVO) {
		File file = this.load(fileOutVO.getId());
		if (file == null) {
			file = File.Factory.newInstance();
		}
		return file;
	}

	/**
	 * Retrieves the entity object that is associated with the specified value object
	 * from the object store. If no such entity object exists in the object store,
	 * a new, blank entity is created
	 */
	private File loadFileFromFileStreamOutVO(FileStreamOutVO fileStreamOutVO) {
		File file = this.load(fileStreamOutVO.getId());
		if (file == null) {
			file = File.Factory.newInstance();
		}
		return file;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public FileContentOutVO toFileContentOutVO(final File entity) {
		return super.toFileContentOutVO(entity);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void toFileContentOutVO(
			File source,
			FileContentOutVO target) {
		super.toFileContentOutVO(source, target);
		MimeType contentType = source.getContentType();
		User modifiedUser = source.getModifiedUser();
		if (contentType != null) {
			target.setContentType(this.getMimeTypeDao().toMimeTypeVO(contentType));
		}
		if (modifiedUser != null) {
			target.setModifiedUser(this.getUserDao().toUserOutVO(modifiedUser));
		}
		if (CommonUtil.getUseFileEncryption(source.getModule())) {
			try {
				if (!CoreUtil.isPassDecryption()) {
					throw new Exception();
				}
				if (source.isExternalFile()) {
					target.setDatas(CommonUtil.inputStreamToByteArray(new VerifyMD5InputStream(CryptoUtil.createDecryptionStream(source.getDataIv(),
							CoreUtil.createFileServiceFileInputStream(source.getExternalFileName())), source.getMd5())));
				} else {
					target.setDatas(CryptoUtil.verifyMD5(CryptoUtil.decrypt(source.getDataIv(), source.getData()), source.getMd5()));
				}
				target.setFileName((String) CryptoUtil.decryptValue(source.getFileNameIv(), source.getEncryptedFileName()));
				target.setDecrypted(true);
			} catch (IllegalArgumentException | IOException e) {
				throw new RuntimeException(e);
			} catch (Exception e) {
				target.setDatas(null);
				target.setFileName(null);
				target.setDecrypted(false);
			}
		} else {
			if (source.isExternalFile()) {
				try {
					target.setDatas(CommonUtil.inputStreamToByteArray(new VerifyMD5InputStream(CoreUtil.createFileServiceFileInputStream(source.getExternalFileName()), source
							.getMd5())));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} else {
				try {
					target.setDatas(CryptoUtil.verifyMD5(source.getData(), source.getMd5()));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			target.setDecrypted(true);
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public FileInVO toFileInVO(final File entity) {
		return super.toFileInVO(entity);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void toFileInVO(
			File source,
			FileInVO target) {
		super.toFileInVO(source, target);
		Inventory inventory = source.getInventory();
		Staff staff = source.getStaff();
		Course course = source.getCourse();
		Trial trial = source.getTrial();
		Proband proband = source.getProband();
		MassMail massMail = source.getMassMail();
		if (inventory != null) {
			target.setInventoryId(inventory.getId());
		}
		if (staff != null) {
			target.setStaffId(staff.getId());
		}
		if (course != null) {
			target.setCourseId(course.getId());
		}
		if (trial != null) {
			target.setTrialId(trial.getId());
		}
		if (proband != null) {
			target.setProbandId(proband.getId());
		}
		if (massMail != null) {
			target.setMassMailId(massMail.getId());
		}
		if (CommonUtil.getUseFileEncryption(source.getModule())) {
			try {
				target.setTitle((String) CryptoUtil.decryptValue(source.getTitleIv(), source.getEncryptedTitle()));
				target.setComment((String) CryptoUtil.decryptValue(source.getCommentIv(), source.getEncryptedComment()));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public FileOutVO toFileOutVO(final File entity) {
		return super.toFileOutVO(entity);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void toFileOutVO(
			File source,
			FileOutVO target) {
		super.toFileOutVO(source, target);
		MimeType contentType = source.getContentType();
		Inventory inventory = source.getInventory();
		Staff staff = source.getStaff();
		Course course = source.getCourse();
		Trial trial = source.getTrial();
		Proband proband = source.getProband();
		User modifiedUser = source.getModifiedUser();
		MassMail massMail = source.getMassMail();
		if (contentType != null) {
			target.setContentType(this.getMimeTypeDao().toMimeTypeVO(contentType));
		}
		if (inventory != null) {
			target.setInventory(this.getInventoryDao().toInventoryOutVO(inventory));
		}
		if (staff != null) {
			target.setStaff(this.getStaffDao().toStaffOutVO(staff));
		}
		if (course != null) {
			target.setCourse(this.getCourseDao().toCourseOutVO(course));
		}
		if (trial != null) {
			target.setTrial(this.getTrialDao().toTrialOutVO(trial));
		}
		if (proband != null) {
			target.setProband(this.getProbandDao().toProbandOutVO(proband));
		}
		if (massMail != null) {
			target.setMassMail(this.getMassMailDao().toMassMailOutVO(massMail));
		}
		if (modifiedUser != null) {
			target.setModifiedUser(this.getUserDao().toUserOutVO(modifiedUser));
		}
		if (CommonUtil.getUseFileEncryption(source.getModule())) {
			try {
				if (!CoreUtil.isPassDecryption()) {
					throw new Exception();
				}
				target.setTitle((String) CryptoUtil.decryptValue(source.getTitleIv(), source.getEncryptedTitle()));
				target.setComment((String) CryptoUtil.decryptValue(source.getCommentIv(), source.getEncryptedComment()));
				target.setFileName((String) CryptoUtil.decryptValue(source.getFileNameIv(), source.getEncryptedFileName()));
				target.setDecrypted(true);
			} catch (Exception e) {
				target.setTitle(null);
				target.setComment(null);
				target.setFileName(null);
				target.setDecrypted(false);
			}
		} else {
			target.setDecrypted(true);
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public FileStreamOutVO toFileStreamOutVO(final File entity) {
		return super.toFileStreamOutVO(entity);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void toFileStreamOutVO(
			File source,
			FileStreamOutVO target) {
		super.toFileStreamOutVO(source, target);
		MimeType contentType = source.getContentType();
		User modifiedUser = source.getModifiedUser();
		if (contentType != null) {
			target.setContentType(this.getMimeTypeDao().toMimeTypeVO(contentType));
		}
		if (modifiedUser != null) {
			target.setModifiedUser(this.getUserDao().toUserOutVO(modifiedUser));
		}
		if (CommonUtil.getUseFileEncryption(source.getModule())) {
			try {
				if (!CoreUtil.isPassDecryption()) {
					throw new Exception();
				}
				if (source.isExternalFile()) {
					target.setStream(new VerifyMD5InputStream(CryptoUtil.createDecryptionStream(source.getDataIv(),
							CoreUtil.createFileServiceFileInputStream(source.getExternalFileName())), source.getMd5()));
				} else {
					if (source.getData() != null) {
						target.setStream(new VerifyMD5InputStream(CryptoUtil.createDecryptionStream(source.getDataIv(), new ByteArrayInputStream(source.getData())), source
								.getMd5()));
					}
				}
				target.setFileName((String) CryptoUtil.decryptValue(source.getFileNameIv(), source.getEncryptedFileName()));
				target.setDecrypted(true);
			} catch (IllegalArgumentException | IOException e) {
				throw new RuntimeException(e);
			} catch (Exception e) {
				if (target.getStream() != null) {
					try {
						target.getStream().close();
					} catch (IOException e1) {
					}
				}
				target.setStream(null);
				target.setFileName(null);
				target.setDecrypted(false);
			}
		} else {
			if (source.isExternalFile()) {
				try {
					target.setStream(new VerifyMD5InputStream(CoreUtil.createFileServiceFileInputStream(source.getExternalFileName()), source.getMd5()));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} else {
				if (source.getData() != null) {
					target.setStream(new ByteArrayInputStream(source.getData()));
				}
			}
			target.setDecrypted(true);
		}
	}
}