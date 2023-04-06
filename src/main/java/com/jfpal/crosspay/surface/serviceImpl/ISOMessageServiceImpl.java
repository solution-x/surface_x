package com.jfpal.crosspay.surface.serviceImpl;

import java.io.InputStream;

import com.jfpal.crosspay.surface.payload.silverlake.IsoProf;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.stereotype.Service;

import com.jfpal.crosspay.surface.payload.IsoData;
import com.jfpal.crosspay.surface.payload.POSPMessage;
import com.jfpal.crosspay.surface.service.ISOMessageService;

@Service
public class ISOMessageServiceImpl implements ISOMessageService {

	private IsoProf isoProf;

	@Override
	public ISOMsg unpackMessage(byte[] isoMessage) throws Exception {

		try {
			InputStream is = getClass().getResourceAsStream("/iso8583bin.xml");
			GenericPackager packager = new GenericPackager(is);
			ISOMsg isoMsg = new ISOMsg();

			isoMsg.setPackager(packager);

			isoMsg.unpack(isoMessage);

			return isoMsg;

		} catch (ISOException isoException) {
			isoException.printStackTrace();
			throw isoException;
		}
	}

	private boolean isValidStr(String str) {
		return (str != null && !str.equals("")) ? true : false;
	}

	private boolean isValidNum(long str) {
		return (str > 0) ? true : false;
	}

	private boolean isValidNumAndZero(long str) {
		return (str >= 0) ? true : false;
	}

	@Override
	public String packMessage(POSPMessage request) throws Exception {
		isoProf = null;
		long[] lFields = null;

		try {
			isoProf = new IsoProf(request.getMessageTypeIdentification(), request.getProcessingCode());

			if (isoProf==null) {
				System.out.println("Invalid ISO Profile");

				throw (new Exception("MTI|ProcessingCode Unrecognized!"));
			}

			lFields = isoProf.getFeilds();

			InputStream is = getClass().getResourceAsStream("/iso8583bin.xml");

			GenericPackager packager = new GenericPackager(is);
			ISOMsg isoMsg = new ISOMsg();
			isoMsg.setPackager(packager);
			isoMsg.setMTI(request.getMessageTypeIdentification());
			IsoData isoData = request.getIsoData();

			for (long lField : lFields) {
				if (lField == 0) {
					break;
				}

				if (lField == 2) {// PAN
					if (isValidStr(isoData.getPrimaryAccount())) {
						isoMsg.set(2, isoData.getPrimaryAccount());
					}
				} else if (lField == 3) {// Processing Code
					if (isValidStr(request.getProcessingCode())) {
						isoMsg.set(3, request.getProcessingCode());
					} else {
						throw new NoSuchFieldException("DE03 - Processing Code");
					}
				} else if (lField == 4) {// Transaction Amount
					if (isValidNum(isoData.getTransactionAmount())) {
						isoMsg.set(4, Long.toString(isoData.getTransactionAmount()));
					} else {
						throw new NoSuchFieldException("DE04 - Transaction Amount");
					}
				} else if (lField == 11) {// System Trace Number
					if (isValidNum(isoData.getSystemTraceNumber())) {
						isoMsg.set(11, Long.toString(isoData.getSystemTraceNumber()));
					} else {
						throw new NoSuchFieldException("DE11 - System Trace Number");
					}
				} else if (lField == 12) {// Transaction Time
					if (isValidStr(isoData.getTransactionTime())) {
						isoMsg.set(12, isoData.getTransactionTime());
					} else {
						throw new NoSuchFieldException("DE12 - Transaction Time");
					}
				}else if (lField == 13) {// Transaction Date
					if (isValidStr(isoData.getTransactionDate())) {
						isoMsg.set(13, isoData.getTransactionDate());
					} else {
						throw new NoSuchFieldException("DE13 - Transaction Date");
					}
				}else if (lField == 14) {// Card Expiry Date
					if (isValidStr(isoData.getCardExpDate())) {
						isoMsg.set(14, isoData.getCardExpDate());
					}
				} else if (lField == 22) {// Entry Mode
					if (isValidStr(isoData.getEntryMode())) {
						isoMsg.set(22, isoData.getEntryMode());
					} else {
						throw new NoSuchFieldException("DE22 - Entry Mode");
					}
				} else if (lField == 23) {// PAN Sequence Number
					if (isValidStr(isoData.getCardSequenceNo())) {
						isoMsg.set(23, isoData.getCardSequenceNo());
					}
				} else if (lField == 24) {// NII
					if (isValidStr(isoData.getNii())) {
						isoMsg.set(24, isoData.getNii());
					} else {
						throw new NoSuchFieldException("DE24 - NII");
					}
				} else if (lField == 25) {// Condition Code
					if (isValidStr(isoData.getConditionCode())) {
						isoMsg.set(25, isoData.getConditionCode());
					} else {
						throw new NoSuchFieldException("DE25 - Condition Code");
					}
				} else if (lField == 35) {// Track 2 Data
					if (isValidStr(isoData.getTrack2Data())) {
						isoMsg.set(35, isoData.getTrack2Data().replace('D', '='));
					}
				} else if (lField == 37) {// RRN
					if (isValidStr(isoData.getRetrievalRefNumber())) {
						isoMsg.set(37, isoData.getRetrievalRefNumber());
					}
				} else if (lField == 38) {// Authorization Code
					if (isValidStr(isoData.getAuthorizationCode())) {
						isoMsg.set(38, isoData.getAuthorizationCode());
					}
				} else if (lField == 39) {// Resp Code
					if (isValidStr(isoData.getRespCode())) {
						isoMsg.set(39, isoData.getRespCode());
					}
				} else if (lField == 41) {// TID
					if (isValidStr(request.getTerminalID())) {
						isoMsg.set(41, request.getTerminalID());
					} else {
						throw new NoSuchFieldException("DE41 - TID");
					}
				} else if (lField == 42) {// MID
					if (isValidStr(request.getMerchantID())) {
						isoMsg.set(42, request.getMerchantID());
					} else {
						throw new NoSuchFieldException("DE42 - MID");
					}
				} else if (lField == 49) {// Currency Code
					if (isValidStr(isoData.getTransactionCurrencyCode())) {
						isoMsg.set(49, isoData.getTransactionCurrencyCode());
					}
				} else if (lField == 52) {// PIN
					if (isValidStr(isoData.getPersonalIdentificationNumber())) {
						isoMsg.set(52, isoData.getPersonalIdentificationNumber());
					}
				} else if (lField == 54) {// Additional Amount
					if (isValidNum(isoData.getTransactionAdditionalAmount())) {
						isoMsg.set(54, Long.toString(isoData.getTransactionAdditionalAmount()));
					}
				} else if (lField == 55) {// IccData
					if (isValidStr(isoData.getSysIccData())) {
						isoMsg.set(55, isoData.getSysIccData());
					}
				} else if (lField == 20061) {// Loyalty Number
					if (isValidStr(isoData.getLoyaltyNumber())) {
						isoMsg.set(61, isoData.getLoyaltyNumber());
					}
				} else if (lField == 62) {// Invoice Number
					if (isValidNum(isoData.getInvoiceNumber())) {
						isoMsg.set(62, ISOUtil.padleft(Long.toString(isoData.getInvoiceNumber()), 6, '0'));
					} else {
						throw new NoSuchFieldException("DE62 - Invoice Number");
					}
				} else if (lField == 20063) {// Loyalty Data
					boolean bSetBit = true;
					String sField63 = "";

					// Transacted Sale Amount
					if (isValidNum(isoData.getTransactionAmount())) {
						sField63 += ISOUtil.padleft(Long.toString(isoData.getTransactionAmount()), 12, '0');
					} else {
						bSetBit = false;
					}

					// Redeemed Amount
					if (isValidNum(isoData.getRedeemedAmount())) {
						sField63 += ISOUtil.padleft(Long.toString(isoData.getRedeemedAmount()), 12, '0');
					} else {
						bSetBit = false;
					}

					// Total Amount
					if (isValidNum(isoData.getTotalTransactionAmount())) {
						sField63 += ISOUtil.padleft(Long.toString(isoData.getTotalTransactionAmount()), 12, '0');
					} else {
						bSetBit = false;
					}

					// Awarded Amount
					if (isValidNum(isoData.getAwardedAmount())) {
						sField63 += ISOUtil.padleft(Long.toString(isoData.getAwardedAmount()), 12, '0');
					} else {
						bSetBit = false;
					}

					// Non-Award Amount
					if (isValidNum(isoData.getNonAwardedAmount())) {
						sField63 += ISOUtil.padleft(Long.toString(isoData.getNonAwardedAmount()), 12, '0');
					} else {
						bSetBit = false;
					}

					// Status Code
					if (isValidStr(isoData.getLoyaltyStatusCode())) {
						sField63 += isoData.getLoyaltyStatusCode();
					} else {
						bSetBit = false;
					}

					if (bSetBit) {
						isoMsg.set(63, sField63);
					}
				} else if (lField == 50063) {
					boolean bSetBit = true;
					String sField63 = "";

					// Total Sale Count
					if (isValidNumAndZero(isoData.getSalesCount())) {
						sField63 += ISOUtil.padleft(Long.toString(isoData.getSalesCount()), 3, '0');
					} else {
						bSetBit = false;
					}

					// Total Sale Amount
					if (isValidNumAndZero(isoData.getSalesAmount())) {
						sField63 += ISOUtil.padleft(Long.toString(isoData.getSalesAmount()), 12, '0');
					} else {
						bSetBit = false;
					}

					// Total Refund Count
					if (isValidNumAndZero(isoData.getRefundCount())) {
						sField63 += ISOUtil.padleft(Long.toString(isoData.getRefundCount()), 3, '0');
					} else {
						bSetBit = false;
					}

					// Total Refund Amount
					if (isValidNumAndZero(isoData.getRefundAmount())) {
						sField63 += ISOUtil.padleft(Long.toString(isoData.getRefundAmount()), 12, '0');
					} else {
						bSetBit = false;
					}

					if (bSetBit) {
						sField63 += ISOUtil.padleft("0", 60, '0');
						isoMsg.set(63, sField63);
					} else {
						throw new Exception("DE50061 - Recon Data");
					}
				} else if (lField == 32060) {
					if (isValidStr(isoData.getOriginalMsgData())) {
						isoMsg.set(60, isoData.getOriginalMsgData());
					} else {
						throw new NoSuchFieldException("DE032060 - Original Data");
					}
				} else if (lField == 50060) {
					if (isValidNum(isoData.getBatchNumber())) {
						isoMsg.set(60, ISOUtil.padleft(Long.toString(isoData.getBatchNumber()), 6, '0'));
					} else {
						throw new NoSuchFieldException("DE50060 - Batch Number");
					}
				}
			}

			byte[] bResult = isoMsg.pack();

			String sTpduStr 			= String.format("600%s0000", isoData.getNii());
			String sIsoMessageNoLength 	= String.format("%s%s", sTpduStr, ISOUtil.byte2hex(bResult));
			int iMessageBuffLength 		= sIsoMessageNoLength.length()/2;
			String sLengthBuff 			= ISOUtil.padleft(Integer.toHexString(iMessageBuffLength), 4, '0');
			String sIsoMessageFull 		= String.format("%s%s", sLengthBuff, sIsoMessageNoLength);
			sIsoMessageFull 			= sIsoMessageFull.toUpperCase();

			System.out.println("REQ[ISO] = " + sIsoMessageFull);

			return sIsoMessageFull;
		} catch (NoSuchFieldException noSuchFieldException) {
			throw noSuchFieldException;
		} catch (Exception e) {
			throw new Exception("General Error - Packing Request");
		}
	}
}
