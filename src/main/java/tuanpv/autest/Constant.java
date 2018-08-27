package tuanpv.autest;

public class Constant {
	public static final boolean IS_DEBUG = true;
	public static final String ENCODE = "UTF-8";

	// define log key
	public static final String LOG_BEGIN = "BEGIN";
	public static final String LOG_END = "END";

	// define file type
	public static final String FILE_TYPE_VM = "vm";
	public static final String FILE_TYPE_XLS = "xls";
	public static final String FILE_TYPE_XLSX = "xlsx";
	public static final String FILE_TYPE_PROPERTIES = "properties";
	public static final String FILE_TYPE_MOBI = "mobi";

	// define file name
	public static final String FILE_APPLICATION_CONTEXT = "applicationContext.xml";
	public static final String FILE_OPTIONS = "options.xml";
	public static final String FILE_PROFILE = "profiles.xml";

	// OPTIONS.XML node and attribute name
	public static final String OPTION_NAME = "option";
	public static final String OPTION_ATTR_OPT = "opt";
	public static final String OPTION_ATTR_LONG_OPT = "longOpt";
	public static final String OPTION_ATTR_REQUIRED = "required";
	public static final String OPTION_ATTR_DESCRIPTION = "description";
	public static final String OPTION_ATTR_HAS_ARGUMENT = "hasArg";

	// PROFILE.XML node and attribute name
	public static final String PROFILE_ACTION_ATTR_BEAN = "bean";
	public static final String PROFILE_ACTION_ATTR_CLASS = "class";
	public static final String PROFILE_NAME = "profile";
	public static final String PROFILE_ATTR_ID = "id";
	public static final String PROFILE_ACTION_NAME = "action";

	// define SUB
	public static final String SUB_SPLITTER = "-";
	public static final String SUB_ARGS = "args";
	public static final String SUB_ACTION = "action";
	public static final String SUB_DATA = "data";
	public static final String SUB_MESSAGE = "msg";

	// define KEY
	public static final String KEY_TYPE = "type";
	public static final String KEY_OUTPUT = "output";
	public static final String KEY_EXTENSION = "ext";
	public static final String KEY_PACKAGE = "package";
	public static final String KEY_PROJECT = "project";
	public static final String KEY_APP = "app";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_TRANSLATE = "translate";
	public static final String KEY_ID = "id";
	public static final String KEY_ENGLISH = "english";
	public static final String KEY_JAPAN = "japan";
	public static final String KEY_CHINA = "china";
	public static final String KEY_CONFIG = "conf";
	public static final String KEY_URL = "url";
	public static final String KEY_TITLE = "title";
	public static final String KEY_NAME = "name";
	public static final String KEY_CODE = "code";
	public static final String KEY_HTML = "html";
	public static final String KEY_CATEGORY = "category";
	public static final String KEY_DESC = "description";
	public static final String KEY_CONTENT = "content";
	public static final String KEY_TAGS = "tags";
	public static final String KEY_LINKS = "links";
	public static final String KEY_CHAPTERS = "chapters";
	public static final String KEY_CHAPTER = "chapter";
	public static final String KEY_INDEX = "index";

	// define PATH
	public static final String PATH_CONFIG = "config";
	public static final String PATH_DATA = "data";
	public static final String PATH_STRUCTURE = "structure";
	public static final String PATH_TEMPLATE = "template";
	public static final String PATH_EXTERNAL = "external";
	public static final String PATH_ETC = "etc";

	// define LOG
	public static final String LOG_STR = "| > %-18s : %s";
	public static final String LOG_MAIN = "- %-20s : -----------";
	public static final String LOG_SUBJECT = "--- %10s %s ---------------";
	public static final String LOG_MAIN_INFO = "- %-5s %14s : %s";
}
