package cn.com.baicdata.stat;

/**
 * Autogenerated by Thrift Compiler (0.8.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Record implements org.apache.thrift.TBase<Record, Record._Fields>,
		java.io.Serializable, Cloneable {
	private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct(
			"Record");

	private static final org.apache.thrift.protocol.TField AREAID_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"Areaid", org.apache.thrift.protocol.TType.STRING, (short) 1);
	private static final org.apache.thrift.protocol.TField DAY_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"Day", org.apache.thrift.protocol.TType.STRING, (short) 2);
	private static final org.apache.thrift.protocol.TField HOUR_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"Hour", org.apache.thrift.protocol.TType.STRING, (short) 3);
	private static final org.apache.thrift.protocol.TField SOURCE_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"Source", org.apache.thrift.protocol.TType.STRING, (short) 4);
	private static final org.apache.thrift.protocol.TField HOST_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"Host", org.apache.thrift.protocol.TType.STRING, (short) 5);
	private static final org.apache.thrift.protocol.TField ADSPACE_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"Adspace", org.apache.thrift.protocol.TType.STRING, (short) 6);
	private static final org.apache.thrift.protocol.TField USERID_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"Userid", org.apache.thrift.protocol.TType.STRING, (short) 7);
	private static final org.apache.thrift.protocol.TField PLANID_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"Planid", org.apache.thrift.protocol.TType.STRING, (short) 8);
	private static final org.apache.thrift.protocol.TField GROUPID_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"Groupid", org.apache.thrift.protocol.TType.STRING, (short) 9);
	private static final org.apache.thrift.protocol.TField ADID_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"Adid", org.apache.thrift.protocol.TType.STRING, (short) 10);
	private static final org.apache.thrift.protocol.TField STUFFID_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"Stuffid", org.apache.thrift.protocol.TType.STRING, (short) 11);

	private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
	static {
		schemes.put(StandardScheme.class, new RecordStandardSchemeFactory());
		schemes.put(TupleScheme.class, new RecordTupleSchemeFactory());
	}

	public String Areaid; // required
	public String Day; // required
	public String Hour; // required
	public String Source; // required
	public String Host; // required
	public String Adspace; // required
	public String Userid; // required
	public String Planid; // required
	public String Groupid; // required
	public String Adid; // required
	public String Stuffid; // required

	/**
	 * The set of fields this struct contains, along with convenience methods
	 * for finding and manipulating them.
	 */
	public enum _Fields implements org.apache.thrift.TFieldIdEnum {
		AREAID((short) 1, "Areaid"), DAY((short) 2, "Day"), HOUR((short) 3,
				"Hour"), SOURCE((short) 4, "Source"), HOST((short) 5, "Host"), ADSPACE(
				(short) 6, "Adspace"), USERID((short) 7, "Userid"), PLANID(
				(short) 8, "Planid"), GROUPID((short) 9, "Groupid"), ADID(
				(short) 10, "Adid"), STUFFID((short) 11, "Stuffid");

		private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

		static {
			for (_Fields field : EnumSet.allOf(_Fields.class)) {
				byName.put(field.getFieldName(), field);
			}
		}

		/**
		 * Find the _Fields constant that matches fieldId, or null if its not
		 * found.
		 */
		public static _Fields findByThriftId(int fieldId) {
			switch (fieldId) {
			case 1: // AREAID
				return AREAID;
			case 2: // DAY
				return DAY;
			case 3: // HOUR
				return HOUR;
			case 4: // SOURCE
				return SOURCE;
			case 5: // HOST
				return HOST;
			case 6: // ADSPACE
				return ADSPACE;
			case 7: // USERID
				return USERID;
			case 8: // PLANID
				return PLANID;
			case 9: // GROUPID
				return GROUPID;
			case 10: // ADID
				return ADID;
			case 11: // STUFFID
				return STUFFID;
			default:
				return null;
			}
		}

		/**
		 * Find the _Fields constant that matches fieldId, throwing an exception
		 * if it is not found.
		 */
		public static _Fields findByThriftIdOrThrow(int fieldId) {
			_Fields fields = findByThriftId(fieldId);
			if (fields == null)
				throw new IllegalArgumentException("Field " + fieldId
						+ " doesn't exist!");
			return fields;
		}

		/**
		 * Find the _Fields constant that matches name, or null if its not
		 * found.
		 */
		public static _Fields findByName(String name) {
			return byName.get(name);
		}

		private final short _thriftId;
		private final String _fieldName;

		_Fields(short thriftId, String fieldName) {
			_thriftId = thriftId;
			_fieldName = fieldName;
		}

		public short getThriftFieldId() {
			return _thriftId;
		}

		public String getFieldName() {
			return _fieldName;
		}
	}

	// isset id assignments
	public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
	static {
		Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(
				_Fields.class);
		tmpMap.put(_Fields.AREAID,
				new org.apache.thrift.meta_data.FieldMetaData("Areaid",
						org.apache.thrift.TFieldRequirementType.DEFAULT,
						new org.apache.thrift.meta_data.FieldValueMetaData(
								org.apache.thrift.protocol.TType.STRING)));
		tmpMap.put(_Fields.DAY, new org.apache.thrift.meta_data.FieldMetaData(
				"Day", org.apache.thrift.TFieldRequirementType.DEFAULT,
				new org.apache.thrift.meta_data.FieldValueMetaData(
						org.apache.thrift.protocol.TType.STRING)));
		tmpMap.put(_Fields.HOUR, new org.apache.thrift.meta_data.FieldMetaData(
				"Hour", org.apache.thrift.TFieldRequirementType.DEFAULT,
				new org.apache.thrift.meta_data.FieldValueMetaData(
						org.apache.thrift.protocol.TType.STRING)));
		tmpMap.put(_Fields.SOURCE,
				new org.apache.thrift.meta_data.FieldMetaData("Source",
						org.apache.thrift.TFieldRequirementType.DEFAULT,
						new org.apache.thrift.meta_data.FieldValueMetaData(
								org.apache.thrift.protocol.TType.STRING)));
		tmpMap.put(_Fields.HOST, new org.apache.thrift.meta_data.FieldMetaData(
				"Host", org.apache.thrift.TFieldRequirementType.DEFAULT,
				new org.apache.thrift.meta_data.FieldValueMetaData(
						org.apache.thrift.protocol.TType.STRING)));
		tmpMap.put(_Fields.ADSPACE,
				new org.apache.thrift.meta_data.FieldMetaData("Adspace",
						org.apache.thrift.TFieldRequirementType.DEFAULT,
						new org.apache.thrift.meta_data.FieldValueMetaData(
								org.apache.thrift.protocol.TType.STRING)));
		tmpMap.put(_Fields.USERID,
				new org.apache.thrift.meta_data.FieldMetaData("Userid",
						org.apache.thrift.TFieldRequirementType.DEFAULT,
						new org.apache.thrift.meta_data.FieldValueMetaData(
								org.apache.thrift.protocol.TType.STRING)));
		tmpMap.put(_Fields.PLANID,
				new org.apache.thrift.meta_data.FieldMetaData("Planid",
						org.apache.thrift.TFieldRequirementType.DEFAULT,
						new org.apache.thrift.meta_data.FieldValueMetaData(
								org.apache.thrift.protocol.TType.STRING)));
		tmpMap.put(_Fields.GROUPID,
				new org.apache.thrift.meta_data.FieldMetaData("Groupid",
						org.apache.thrift.TFieldRequirementType.DEFAULT,
						new org.apache.thrift.meta_data.FieldValueMetaData(
								org.apache.thrift.protocol.TType.STRING)));
		tmpMap.put(_Fields.ADID, new org.apache.thrift.meta_data.FieldMetaData(
				"Adid", org.apache.thrift.TFieldRequirementType.DEFAULT,
				new org.apache.thrift.meta_data.FieldValueMetaData(
						org.apache.thrift.protocol.TType.STRING)));
		tmpMap.put(_Fields.STUFFID,
				new org.apache.thrift.meta_data.FieldMetaData("Stuffid",
						org.apache.thrift.TFieldRequirementType.DEFAULT,
						new org.apache.thrift.meta_data.FieldValueMetaData(
								org.apache.thrift.protocol.TType.STRING)));
		metaDataMap = Collections.unmodifiableMap(tmpMap);
		org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(
				Record.class, metaDataMap);
	}

	public Record() {
		this.Areaid = "";

		this.Day = "";

		this.Hour = "";

		this.Source = "";

		this.Host = "";

		this.Adspace = "";

		this.Userid = "";

		this.Planid = "";

		this.Groupid = "";

		this.Adid = "";

		this.Stuffid = "";

	}

	public Record(String Areaid, String Day, String Hour, String Source,
			String Host, String Adspace, String Userid, String Planid,
			String Groupid, String Adid, String Stuffid) {
		this();
		this.Areaid = Areaid;
		this.Day = Day;
		this.Hour = Hour;
		this.Source = Source;
		this.Host = Host;
		this.Adspace = Adspace;
		this.Userid = Userid;
		this.Planid = Planid;
		this.Groupid = Groupid;
		this.Adid = Adid;
		this.Stuffid = Stuffid;
	}

	/**
	 * Performs a deep copy on <i>other</i>.
	 */
	public Record(Record other) {
		if (other.isSetAreaid()) {
			this.Areaid = other.Areaid;
		}
		if (other.isSetDay()) {
			this.Day = other.Day;
		}
		if (other.isSetHour()) {
			this.Hour = other.Hour;
		}
		if (other.isSetSource()) {
			this.Source = other.Source;
		}
		if (other.isSetHost()) {
			this.Host = other.Host;
		}
		if (other.isSetAdspace()) {
			this.Adspace = other.Adspace;
		}
		if (other.isSetUserid()) {
			this.Userid = other.Userid;
		}
		if (other.isSetPlanid()) {
			this.Planid = other.Planid;
		}
		if (other.isSetGroupid()) {
			this.Groupid = other.Groupid;
		}
		if (other.isSetAdid()) {
			this.Adid = other.Adid;
		}
		if (other.isSetStuffid()) {
			this.Stuffid = other.Stuffid;
		}
	}

	public Record deepCopy() {
		return new Record(this);
	}

	@Override
	public void clear() {
		this.Areaid = "";

		this.Day = "";

		this.Hour = "";

		this.Source = "";

		this.Host = "";

		this.Adspace = "";

		this.Userid = "";

		this.Planid = "";

		this.Groupid = "";

		this.Adid = "";

		this.Stuffid = "";

	}

	public String getAreaid() {
		return this.Areaid;
	}

	public Record setAreaid(String Areaid) {
		this.Areaid = Areaid;
		return this;
	}

	public void unsetAreaid() {
		this.Areaid = null;
	}

	/**
	 * Returns true if field Areaid is set (has been assigned a value) and false
	 * otherwise
	 */
	public boolean isSetAreaid() {
		return this.Areaid != null;
	}

	public void setAreaidIsSet(boolean value) {
		if (!value) {
			this.Areaid = null;
		}
	}

	public String getDay() {
		return this.Day;
	}

	public Record setDay(String Day) {
		this.Day = Day;
		return this;
	}

	public void unsetDay() {
		this.Day = null;
	}

	/**
	 * Returns true if field Day is set (has been assigned a value) and false
	 * otherwise
	 */
	public boolean isSetDay() {
		return this.Day != null;
	}

	public void setDayIsSet(boolean value) {
		if (!value) {
			this.Day = null;
		}
	}

	public String getHour() {
		return this.Hour;
	}

	public Record setHour(String Hour) {
		this.Hour = Hour;
		return this;
	}

	public void unsetHour() {
		this.Hour = null;
	}

	/**
	 * Returns true if field Hour is set (has been assigned a value) and false
	 * otherwise
	 */
	public boolean isSetHour() {
		return this.Hour != null;
	}

	public void setHourIsSet(boolean value) {
		if (!value) {
			this.Hour = null;
		}
	}

	public String getSource() {
		return this.Source;
	}

	public Record setSource(String Source) {
		this.Source = Source;
		return this;
	}

	public void unsetSource() {
		this.Source = null;
	}

	/**
	 * Returns true if field Source is set (has been assigned a value) and false
	 * otherwise
	 */
	public boolean isSetSource() {
		return this.Source != null;
	}

	public void setSourceIsSet(boolean value) {
		if (!value) {
			this.Source = null;
		}
	}

	public String getHost() {
		return this.Host;
	}

	public Record setHost(String Host) {
		this.Host = Host;
		return this;
	}

	public void unsetHost() {
		this.Host = null;
	}

	/**
	 * Returns true if field Host is set (has been assigned a value) and false
	 * otherwise
	 */
	public boolean isSetHost() {
		return this.Host != null;
	}

	public void setHostIsSet(boolean value) {
		if (!value) {
			this.Host = null;
		}
	}

	public String getAdspace() {
		return this.Adspace;
	}

	public Record setAdspace(String Adspace) {
		this.Adspace = Adspace;
		return this;
	}

	public void unsetAdspace() {
		this.Adspace = null;
	}

	/**
	 * Returns true if field Adspace is set (has been assigned a value) and
	 * false otherwise
	 */
	public boolean isSetAdspace() {
		return this.Adspace != null;
	}

	public void setAdspaceIsSet(boolean value) {
		if (!value) {
			this.Adspace = null;
		}
	}

	public String getUserid() {
		return this.Userid;
	}

	public Record setUserid(String Userid) {
		this.Userid = Userid;
		return this;
	}

	public void unsetUserid() {
		this.Userid = null;
	}

	/**
	 * Returns true if field Userid is set (has been assigned a value) and false
	 * otherwise
	 */
	public boolean isSetUserid() {
		return this.Userid != null;
	}

	public void setUseridIsSet(boolean value) {
		if (!value) {
			this.Userid = null;
		}
	}

	public String getPlanid() {
		return this.Planid;
	}

	public Record setPlanid(String Planid) {
		this.Planid = Planid;
		return this;
	}

	public void unsetPlanid() {
		this.Planid = null;
	}

	/**
	 * Returns true if field Planid is set (has been assigned a value) and false
	 * otherwise
	 */
	public boolean isSetPlanid() {
		return this.Planid != null;
	}

	public void setPlanidIsSet(boolean value) {
		if (!value) {
			this.Planid = null;
		}
	}

	public String getGroupid() {
		return this.Groupid;
	}

	public Record setGroupid(String Groupid) {
		this.Groupid = Groupid;
		return this;
	}

	public void unsetGroupid() {
		this.Groupid = null;
	}

	/**
	 * Returns true if field Groupid is set (has been assigned a value) and
	 * false otherwise
	 */
	public boolean isSetGroupid() {
		return this.Groupid != null;
	}

	public void setGroupidIsSet(boolean value) {
		if (!value) {
			this.Groupid = null;
		}
	}

	public String getAdid() {
		return this.Adid;
	}

	public Record setAdid(String Adid) {
		this.Adid = Adid;
		return this;
	}

	public void unsetAdid() {
		this.Adid = null;
	}

	/**
	 * Returns true if field Adid is set (has been assigned a value) and false
	 * otherwise
	 */
	public boolean isSetAdid() {
		return this.Adid != null;
	}

	public void setAdidIsSet(boolean value) {
		if (!value) {
			this.Adid = null;
		}
	}

	public String getStuffid() {
		return this.Stuffid;
	}

	public Record setStuffid(String Stuffid) {
		this.Stuffid = Stuffid;
		return this;
	}

	public void unsetStuffid() {
		this.Stuffid = null;
	}

	/**
	 * Returns true if field Stuffid is set (has been assigned a value) and
	 * false otherwise
	 */
	public boolean isSetStuffid() {
		return this.Stuffid != null;
	}

	public void setStuffidIsSet(boolean value) {
		if (!value) {
			this.Stuffid = null;
		}
	}

	public void setFieldValue(_Fields field, Object value) {
		switch (field) {
		case AREAID:
			if (value == null) {
				unsetAreaid();
			} else {
				setAreaid((String) value);
			}
			break;

		case DAY:
			if (value == null) {
				unsetDay();
			} else {
				setDay((String) value);
			}
			break;

		case HOUR:
			if (value == null) {
				unsetHour();
			} else {
				setHour((String) value);
			}
			break;

		case SOURCE:
			if (value == null) {
				unsetSource();
			} else {
				setSource((String) value);
			}
			break;

		case HOST:
			if (value == null) {
				unsetHost();
			} else {
				setHost((String) value);
			}
			break;

		case ADSPACE:
			if (value == null) {
				unsetAdspace();
			} else {
				setAdspace((String) value);
			}
			break;

		case USERID:
			if (value == null) {
				unsetUserid();
			} else {
				setUserid((String) value);
			}
			break;

		case PLANID:
			if (value == null) {
				unsetPlanid();
			} else {
				setPlanid((String) value);
			}
			break;

		case GROUPID:
			if (value == null) {
				unsetGroupid();
			} else {
				setGroupid((String) value);
			}
			break;

		case ADID:
			if (value == null) {
				unsetAdid();
			} else {
				setAdid((String) value);
			}
			break;

		case STUFFID:
			if (value == null) {
				unsetStuffid();
			} else {
				setStuffid((String) value);
			}
			break;

		}
	}

	public Object getFieldValue(_Fields field) {
		switch (field) {
		case AREAID:
			return getAreaid();

		case DAY:
			return getDay();

		case HOUR:
			return getHour();

		case SOURCE:
			return getSource();

		case HOST:
			return getHost();

		case ADSPACE:
			return getAdspace();

		case USERID:
			return getUserid();

		case PLANID:
			return getPlanid();

		case GROUPID:
			return getGroupid();

		case ADID:
			return getAdid();

		case STUFFID:
			return getStuffid();

		}
		throw new IllegalStateException();
	}

	/**
	 * Returns true if field corresponding to fieldID is set (has been assigned
	 * a value) and false otherwise
	 */
	public boolean isSet(_Fields field) {
		if (field == null) {
			throw new IllegalArgumentException();
		}

		switch (field) {
		case AREAID:
			return isSetAreaid();
		case DAY:
			return isSetDay();
		case HOUR:
			return isSetHour();
		case SOURCE:
			return isSetSource();
		case HOST:
			return isSetHost();
		case ADSPACE:
			return isSetAdspace();
		case USERID:
			return isSetUserid();
		case PLANID:
			return isSetPlanid();
		case GROUPID:
			return isSetGroupid();
		case ADID:
			return isSetAdid();
		case STUFFID:
			return isSetStuffid();
		}
		throw new IllegalStateException();
	}

	@Override
	public boolean equals(Object that) {
		if (that == null)
			return false;
		if (that instanceof Record)
			return this.equals((Record) that);
		return false;
	}

	public boolean equals(Record that) {
		if (that == null)
			return false;

		boolean this_present_Areaid = true && this.isSetAreaid();
		boolean that_present_Areaid = true && that.isSetAreaid();
		if (this_present_Areaid || that_present_Areaid) {
			if (!(this_present_Areaid && that_present_Areaid))
				return false;
			if (!this.Areaid.equals(that.Areaid))
				return false;
		}

		boolean this_present_Day = true && this.isSetDay();
		boolean that_present_Day = true && that.isSetDay();
		if (this_present_Day || that_present_Day) {
			if (!(this_present_Day && that_present_Day))
				return false;
			if (!this.Day.equals(that.Day))
				return false;
		}

		boolean this_present_Hour = true && this.isSetHour();
		boolean that_present_Hour = true && that.isSetHour();
		if (this_present_Hour || that_present_Hour) {
			if (!(this_present_Hour && that_present_Hour))
				return false;
			if (!this.Hour.equals(that.Hour))
				return false;
		}

		boolean this_present_Source = true && this.isSetSource();
		boolean that_present_Source = true && that.isSetSource();
		if (this_present_Source || that_present_Source) {
			if (!(this_present_Source && that_present_Source))
				return false;
			if (!this.Source.equals(that.Source))
				return false;
		}

		boolean this_present_Host = true && this.isSetHost();
		boolean that_present_Host = true && that.isSetHost();
		if (this_present_Host || that_present_Host) {
			if (!(this_present_Host && that_present_Host))
				return false;
			if (!this.Host.equals(that.Host))
				return false;
		}

		boolean this_present_Adspace = true && this.isSetAdspace();
		boolean that_present_Adspace = true && that.isSetAdspace();
		if (this_present_Adspace || that_present_Adspace) {
			if (!(this_present_Adspace && that_present_Adspace))
				return false;
			if (!this.Adspace.equals(that.Adspace))
				return false;
		}

		boolean this_present_Userid = true && this.isSetUserid();
		boolean that_present_Userid = true && that.isSetUserid();
		if (this_present_Userid || that_present_Userid) {
			if (!(this_present_Userid && that_present_Userid))
				return false;
			if (!this.Userid.equals(that.Userid))
				return false;
		}

		boolean this_present_Planid = true && this.isSetPlanid();
		boolean that_present_Planid = true && that.isSetPlanid();
		if (this_present_Planid || that_present_Planid) {
			if (!(this_present_Planid && that_present_Planid))
				return false;
			if (!this.Planid.equals(that.Planid))
				return false;
		}

		boolean this_present_Groupid = true && this.isSetGroupid();
		boolean that_present_Groupid = true && that.isSetGroupid();
		if (this_present_Groupid || that_present_Groupid) {
			if (!(this_present_Groupid && that_present_Groupid))
				return false;
			if (!this.Groupid.equals(that.Groupid))
				return false;
		}

		boolean this_present_Adid = true && this.isSetAdid();
		boolean that_present_Adid = true && that.isSetAdid();
		if (this_present_Adid || that_present_Adid) {
			if (!(this_present_Adid && that_present_Adid))
				return false;
			if (!this.Adid.equals(that.Adid))
				return false;
		}

		boolean this_present_Stuffid = true && this.isSetStuffid();
		boolean that_present_Stuffid = true && that.isSetStuffid();
		if (this_present_Stuffid || that_present_Stuffid) {
			if (!(this_present_Stuffid && that_present_Stuffid))
				return false;
			if (!this.Stuffid.equals(that.Stuffid))
				return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		// return 0;
		return this.toString().hashCode();
	}

	public int compareTo(Record other) {
		if (!getClass().equals(other.getClass())) {
			return getClass().getName().compareTo(other.getClass().getName());
		}

		int lastComparison = 0;
		Record typedOther = (Record) other;

		lastComparison = Boolean.valueOf(isSetAreaid()).compareTo(
				typedOther.isSetAreaid());
		if (lastComparison != 0) {
			return lastComparison;
		}
		if (isSetAreaid()) {
			lastComparison = org.apache.thrift.TBaseHelper.compareTo(
					this.Areaid, typedOther.Areaid);
			if (lastComparison != 0) {
				return lastComparison;
			}
		}
		lastComparison = Boolean.valueOf(isSetDay()).compareTo(
				typedOther.isSetDay());
		if (lastComparison != 0) {
			return lastComparison;
		}
		if (isSetDay()) {
			lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.Day,
					typedOther.Day);
			if (lastComparison != 0) {
				return lastComparison;
			}
		}
		lastComparison = Boolean.valueOf(isSetHour()).compareTo(
				typedOther.isSetHour());
		if (lastComparison != 0) {
			return lastComparison;
		}
		if (isSetHour()) {
			lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.Hour,
					typedOther.Hour);
			if (lastComparison != 0) {
				return lastComparison;
			}
		}
		lastComparison = Boolean.valueOf(isSetSource()).compareTo(
				typedOther.isSetSource());
		if (lastComparison != 0) {
			return lastComparison;
		}
		if (isSetSource()) {
			lastComparison = org.apache.thrift.TBaseHelper.compareTo(
					this.Source, typedOther.Source);
			if (lastComparison != 0) {
				return lastComparison;
			}
		}
		lastComparison = Boolean.valueOf(isSetHost()).compareTo(
				typedOther.isSetHost());
		if (lastComparison != 0) {
			return lastComparison;
		}
		if (isSetHost()) {
			lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.Host,
					typedOther.Host);
			if (lastComparison != 0) {
				return lastComparison;
			}
		}
		lastComparison = Boolean.valueOf(isSetAdspace()).compareTo(
				typedOther.isSetAdspace());
		if (lastComparison != 0) {
			return lastComparison;
		}
		if (isSetAdspace()) {
			lastComparison = org.apache.thrift.TBaseHelper.compareTo(
					this.Adspace, typedOther.Adspace);
			if (lastComparison != 0) {
				return lastComparison;
			}
		}
		lastComparison = Boolean.valueOf(isSetUserid()).compareTo(
				typedOther.isSetUserid());
		if (lastComparison != 0) {
			return lastComparison;
		}
		if (isSetUserid()) {
			lastComparison = org.apache.thrift.TBaseHelper.compareTo(
					this.Userid, typedOther.Userid);
			if (lastComparison != 0) {
				return lastComparison;
			}
		}
		lastComparison = Boolean.valueOf(isSetPlanid()).compareTo(
				typedOther.isSetPlanid());
		if (lastComparison != 0) {
			return lastComparison;
		}
		if (isSetPlanid()) {
			lastComparison = org.apache.thrift.TBaseHelper.compareTo(
					this.Planid, typedOther.Planid);
			if (lastComparison != 0) {
				return lastComparison;
			}
		}
		lastComparison = Boolean.valueOf(isSetGroupid()).compareTo(
				typedOther.isSetGroupid());
		if (lastComparison != 0) {
			return lastComparison;
		}
		if (isSetGroupid()) {
			lastComparison = org.apache.thrift.TBaseHelper.compareTo(
					this.Groupid, typedOther.Groupid);
			if (lastComparison != 0) {
				return lastComparison;
			}
		}
		lastComparison = Boolean.valueOf(isSetAdid()).compareTo(
				typedOther.isSetAdid());
		if (lastComparison != 0) {
			return lastComparison;
		}
		if (isSetAdid()) {
			lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.Adid,
					typedOther.Adid);
			if (lastComparison != 0) {
				return lastComparison;
			}
		}
		lastComparison = Boolean.valueOf(isSetStuffid()).compareTo(
				typedOther.isSetStuffid());
		if (lastComparison != 0) {
			return lastComparison;
		}
		if (isSetStuffid()) {
			lastComparison = org.apache.thrift.TBaseHelper.compareTo(
					this.Stuffid, typedOther.Stuffid);
			if (lastComparison != 0) {
				return lastComparison;
			}
		}
		return 0;
	}

	public _Fields fieldForId(int fieldId) {
		return _Fields.findByThriftId(fieldId);
	}

	public void read(org.apache.thrift.protocol.TProtocol iprot)
			throws org.apache.thrift.TException {
		schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
	}

	public void write(org.apache.thrift.protocol.TProtocol oprot)
			throws org.apache.thrift.TException {
		schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Record(");
		boolean first = true;

		sb.append("Areaid:");
		if (this.Areaid == null) {
			sb.append("null");
		} else {
			sb.append(this.Areaid);
		}
		first = false;
		if (!first)
			sb.append(", ");
		sb.append("Day:");
		if (this.Day == null) {
			sb.append("null");
		} else {
			sb.append(this.Day);
		}
		first = false;
		if (!first)
			sb.append(", ");
		sb.append("Hour:");
		if (this.Hour == null) {
			sb.append("null");
		} else {
			sb.append(this.Hour);
		}
		first = false;
		if (!first)
			sb.append(", ");
		sb.append("Source:");
		if (this.Source == null) {
			sb.append("null");
		} else {
			sb.append(this.Source);
		}
		first = false;
		if (!first)
			sb.append(", ");
		sb.append("Host:");
		if (this.Host == null) {
			sb.append("null");
		} else {
			sb.append(this.Host);
		}
		first = false;
		if (!first)
			sb.append(", ");
		sb.append("Adspace:");
		if (this.Adspace == null) {
			sb.append("null");
		} else {
			sb.append(this.Adspace);
		}
		first = false;
		if (!first)
			sb.append(", ");
		sb.append("Userid:");
		if (this.Userid == null) {
			sb.append("null");
		} else {
			sb.append(this.Userid);
		}
		first = false;
		if (!first)
			sb.append(", ");
		sb.append("Planid:");
		if (this.Planid == null) {
			sb.append("null");
		} else {
			sb.append(this.Planid);
		}
		first = false;
		if (!first)
			sb.append(", ");
		sb.append("Groupid:");
		if (this.Groupid == null) {
			sb.append("null");
		} else {
			sb.append(this.Groupid);
		}
		first = false;
		if (!first)
			sb.append(", ");
		sb.append("Adid:");
		if (this.Adid == null) {
			sb.append("null");
		} else {
			sb.append(this.Adid);
		}
		first = false;
		if (!first)
			sb.append(", ");
		sb.append("Stuffid:");
		if (this.Stuffid == null) {
			sb.append("null");
		} else {
			sb.append(this.Stuffid);
		}
		first = false;
		sb.append(")");
		return sb.toString();
	}

	public void validate() throws org.apache.thrift.TException {
		// check for required fields
	}

	private void writeObject(java.io.ObjectOutputStream out)
			throws java.io.IOException {
		try {
			write(new org.apache.thrift.protocol.TCompactProtocol(
					new org.apache.thrift.transport.TIOStreamTransport(out)));
		} catch (org.apache.thrift.TException te) {
			throw new java.io.IOException(te);
		}
	}

	private void readObject(java.io.ObjectInputStream in)
			throws java.io.IOException, ClassNotFoundException {
		try {
			read(new org.apache.thrift.protocol.TCompactProtocol(
					new org.apache.thrift.transport.TIOStreamTransport(in)));
		} catch (org.apache.thrift.TException te) {
			throw new java.io.IOException(te);
		}
	}

	private static class RecordStandardSchemeFactory implements SchemeFactory {
		public RecordStandardScheme getScheme() {
			return new RecordStandardScheme();
		}
	}

	private static class RecordStandardScheme extends StandardScheme<Record> {

		public void read(org.apache.thrift.protocol.TProtocol iprot,
				Record struct) throws org.apache.thrift.TException {
			org.apache.thrift.protocol.TField schemeField;
			iprot.readStructBegin();
			while (true) {
				schemeField = iprot.readFieldBegin();
				if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
					break;
				}
				switch (schemeField.id) {
				case 1: // AREAID
					if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
						struct.Areaid = iprot.readString();
						struct.setAreaidIsSet(true);
					} else {
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
								schemeField.type);
					}
					break;
				case 2: // DAY
					if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
						struct.Day = iprot.readString();
						struct.setDayIsSet(true);
					} else {
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
								schemeField.type);
					}
					break;
				case 3: // HOUR
					if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
						struct.Hour = iprot.readString();
						struct.setHourIsSet(true);
					} else {
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
								schemeField.type);
					}
					break;
				case 4: // SOURCE
					if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
						struct.Source = iprot.readString();
						struct.setSourceIsSet(true);
					} else {
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
								schemeField.type);
					}
					break;
				case 5: // HOST
					if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
						struct.Host = iprot.readString();
						struct.setHostIsSet(true);
					} else {
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
								schemeField.type);
					}
					break;
				case 6: // ADSPACE
					if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
						struct.Adspace = iprot.readString();
						struct.setAdspaceIsSet(true);
					} else {
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
								schemeField.type);
					}
					break;
				case 7: // USERID
					if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
						struct.Userid = iprot.readString();
						struct.setUseridIsSet(true);
					} else {
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
								schemeField.type);
					}
					break;
				case 8: // PLANID
					if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
						struct.Planid = iprot.readString();
						struct.setPlanidIsSet(true);
					} else {
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
								schemeField.type);
					}
					break;
				case 9: // GROUPID
					if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
						struct.Groupid = iprot.readString();
						struct.setGroupidIsSet(true);
					} else {
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
								schemeField.type);
					}
					break;
				case 10: // ADID
					if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
						struct.Adid = iprot.readString();
						struct.setAdidIsSet(true);
					} else {
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
								schemeField.type);
					}
					break;
				case 11: // STUFFID
					if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
						struct.Stuffid = iprot.readString();
						struct.setStuffidIsSet(true);
					} else {
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
								schemeField.type);
					}
					break;
				default:
					org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
							schemeField.type);
				}
				iprot.readFieldEnd();
			}
			iprot.readStructEnd();

			// check for required fields of primitive type, which can't be
			// checked in the validate method
			struct.validate();
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot,
				Record struct) throws org.apache.thrift.TException {
			struct.validate();

			oprot.writeStructBegin(STRUCT_DESC);
			if (struct.Areaid != null) {
				oprot.writeFieldBegin(AREAID_FIELD_DESC);
				oprot.writeString(struct.Areaid);
				oprot.writeFieldEnd();
			}
			if (struct.Day != null) {
				oprot.writeFieldBegin(DAY_FIELD_DESC);
				oprot.writeString(struct.Day);
				oprot.writeFieldEnd();
			}
			if (struct.Hour != null) {
				oprot.writeFieldBegin(HOUR_FIELD_DESC);
				oprot.writeString(struct.Hour);
				oprot.writeFieldEnd();
			}
			if (struct.Source != null) {
				oprot.writeFieldBegin(SOURCE_FIELD_DESC);
				oprot.writeString(struct.Source);
				oprot.writeFieldEnd();
			}
			if (struct.Host != null) {
				oprot.writeFieldBegin(HOST_FIELD_DESC);
				oprot.writeString(struct.Host);
				oprot.writeFieldEnd();
			}
			if (struct.Adspace != null) {
				oprot.writeFieldBegin(ADSPACE_FIELD_DESC);
				oprot.writeString(struct.Adspace);
				oprot.writeFieldEnd();
			}
			if (struct.Userid != null) {
				oprot.writeFieldBegin(USERID_FIELD_DESC);
				oprot.writeString(struct.Userid);
				oprot.writeFieldEnd();
			}
			if (struct.Planid != null) {
				oprot.writeFieldBegin(PLANID_FIELD_DESC);
				oprot.writeString(struct.Planid);
				oprot.writeFieldEnd();
			}
			if (struct.Groupid != null) {
				oprot.writeFieldBegin(GROUPID_FIELD_DESC);
				oprot.writeString(struct.Groupid);
				oprot.writeFieldEnd();
			}
			if (struct.Adid != null) {
				oprot.writeFieldBegin(ADID_FIELD_DESC);
				oprot.writeString(struct.Adid);
				oprot.writeFieldEnd();
			}
			if (struct.Stuffid != null) {
				oprot.writeFieldBegin(STUFFID_FIELD_DESC);
				oprot.writeString(struct.Stuffid);
				oprot.writeFieldEnd();
			}
			oprot.writeFieldStop();
			oprot.writeStructEnd();
		}

	}

	private static class RecordTupleSchemeFactory implements SchemeFactory {
		public RecordTupleScheme getScheme() {
			return new RecordTupleScheme();
		}
	}

	private static class RecordTupleScheme extends TupleScheme<Record> {

		@Override
		public void write(org.apache.thrift.protocol.TProtocol prot,
				Record struct) throws org.apache.thrift.TException {
			TTupleProtocol oprot = (TTupleProtocol) prot;
			BitSet optionals = new BitSet();
			if (struct.isSetAreaid()) {
				optionals.set(0);
			}
			if (struct.isSetDay()) {
				optionals.set(1);
			}
			if (struct.isSetHour()) {
				optionals.set(2);
			}
			if (struct.isSetSource()) {
				optionals.set(3);
			}
			if (struct.isSetHost()) {
				optionals.set(4);
			}
			if (struct.isSetAdspace()) {
				optionals.set(5);
			}
			if (struct.isSetUserid()) {
				optionals.set(6);
			}
			if (struct.isSetPlanid()) {
				optionals.set(7);
			}
			if (struct.isSetGroupid()) {
				optionals.set(8);
			}
			if (struct.isSetAdid()) {
				optionals.set(9);
			}
			if (struct.isSetStuffid()) {
				optionals.set(10);
			}
			oprot.writeBitSet(optionals, 11);
			if (struct.isSetAreaid()) {
				oprot.writeString(struct.Areaid);
			}
			if (struct.isSetDay()) {
				oprot.writeString(struct.Day);
			}
			if (struct.isSetHour()) {
				oprot.writeString(struct.Hour);
			}
			if (struct.isSetSource()) {
				oprot.writeString(struct.Source);
			}
			if (struct.isSetHost()) {
				oprot.writeString(struct.Host);
			}
			if (struct.isSetAdspace()) {
				oprot.writeString(struct.Adspace);
			}
			if (struct.isSetUserid()) {
				oprot.writeString(struct.Userid);
			}
			if (struct.isSetPlanid()) {
				oprot.writeString(struct.Planid);
			}
			if (struct.isSetGroupid()) {
				oprot.writeString(struct.Groupid);
			}
			if (struct.isSetAdid()) {
				oprot.writeString(struct.Adid);
			}
			if (struct.isSetStuffid()) {
				oprot.writeString(struct.Stuffid);
			}
		}

		@Override
		public void read(org.apache.thrift.protocol.TProtocol prot,
				Record struct) throws org.apache.thrift.TException {
			TTupleProtocol iprot = (TTupleProtocol) prot;
			BitSet incoming = iprot.readBitSet(11);
			if (incoming.get(0)) {
				struct.Areaid = iprot.readString();
				struct.setAreaidIsSet(true);
			}
			if (incoming.get(1)) {
				struct.Day = iprot.readString();
				struct.setDayIsSet(true);
			}
			if (incoming.get(2)) {
				struct.Hour = iprot.readString();
				struct.setHourIsSet(true);
			}
			if (incoming.get(3)) {
				struct.Source = iprot.readString();
				struct.setSourceIsSet(true);
			}
			if (incoming.get(4)) {
				struct.Host = iprot.readString();
				struct.setHostIsSet(true);
			}
			if (incoming.get(5)) {
				struct.Adspace = iprot.readString();
				struct.setAdspaceIsSet(true);
			}
			if (incoming.get(6)) {
				struct.Userid = iprot.readString();
				struct.setUseridIsSet(true);
			}
			if (incoming.get(7)) {
				struct.Planid = iprot.readString();
				struct.setPlanidIsSet(true);
			}
			if (incoming.get(8)) {
				struct.Groupid = iprot.readString();
				struct.setGroupidIsSet(true);
			}
			if (incoming.get(9)) {
				struct.Adid = iprot.readString();
				struct.setAdidIsSet(true);
			}
			if (incoming.get(10)) {
				struct.Stuffid = iprot.readString();
				struct.setStuffidIsSet(true);
			}
		}
	}

}