package main;

import main.Analytics.*;

public class Filter
{

	public enum FilterTypes 
	{
		SERVICE_ID, 
		SERVICE_VARIATION_ID, 
		QUESTION_TYPE_ID, 
		QUESTION_CATEGORY_ID, 
		QUESTION_SUBCATEGORY_ID, 
		TYPE_ANSWER, 
		DATE
	}

	public interface IFilter
	{
		boolean filter(RecordWT recordWT, RecordQuery recordQuery);
	}

	public class FilterServiceId implements IFilter
	{
		public boolean filter(RecordWT recordWT, RecordQuery recordQuery)
		{
			if (recordWT.getServiceId() != null 
					&& recordWT.getServiceId().intValue() == recordQuery.getServiceId().intValue())
				return true;
			else
				return false;
		}
	}

	public class FilterServiceVariationId implements IFilter
	{
		public boolean filter(RecordWT recordWT, RecordQuery recordQuery)
		{
			if (recordWT.getServiceVariationId() != null
					&& recordWT.getServiceVariationId().intValue() == recordQuery.getServiceVariationId().intValue())
				return true;
			else
				return false;
		}
	}

	public class FilterQuestionTypeId implements IFilter
	{
		public boolean filter(RecordWT recordWT, RecordQuery recordQuery)
		{
			if (recordWT.getQuestionTypeId() != null
					&& recordWT.getQuestionTypeId().intValue() == recordQuery.getQuestionTypeId().intValue())
				return true;
			else
				return false;
		}
	}

	public class FilterQuestionSubCategoryId implements IFilter
	{
		public boolean filter(RecordWT recordWT, RecordQuery recordQuery)
		{
			if (recordWT.getQuestionSubCategoryId() != null
					&& recordWT.getQuestionSubCategoryId().intValue() == recordQuery.getQuestionSubCategoryId().intValue())
				return true;
			else
				return false;
		}
	}

	public class FilterQuestionCategoryId implements IFilter
	{
		public boolean filter(RecordWT recordWT, RecordQuery recordQuery)
		{
			if (recordWT.getQuestionCategoryId() != null
					&& recordWT.getQuestionCategoryId().intValue() == recordQuery.getQuestionCategoryId().intValue())
				return true;
			else
				return false;
		}
	}

	public class FilterDate implements IFilter
	{
		public boolean filter(RecordWT recordWT, RecordQuery recordQuery)
		{
			if (recordWT.getDate() != null
					&& (recordQuery.getDateTo() != null && recordWT.getDate().after(recordQuery.getDateFrom()) 
							&& recordWT.getDate().before(recordQuery.getDateTo()))
					|| (recordQuery.getDateFrom() == null && recordWT.getDate().equals(recordQuery.getDateFrom())))
				return true;
			else
				return false;
		}
	}

	public class FactoryFilter
	{
		public IFilter getFilter(FilterTypes type)
		{
			IFilter ret = null;
			switch (type)
			{
			case SERVICE_ID:
				ret = new FilterServiceId();
				break;
			case SERVICE_VARIATION_ID:
				ret = new FilterServiceVariationId();
				break;
			case QUESTION_TYPE_ID:
				ret = new FilterQuestionTypeId();
				break;
			case QUESTION_CATEGORY_ID:
				ret = new FilterQuestionCategoryId();
				break;
			case QUESTION_SUBCATEGORY_ID:
				ret = new FilterQuestionSubCategoryId();
				break;
			case DATE:
				ret = new FilterDate();
				break;
			default:
				throw new IllegalArgumentException("Wrong filter type:" + type);
			}
			return ret;
		}
	}
}
