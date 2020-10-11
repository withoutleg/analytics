package main;

import java.util.*;
import main.Filter.*;

class Analytics
{
	// base class for records content the common data
	public static class Record
	{
		private Integer recordId;
		private Integer serviceId;
		private Integer serviceVariationId;
		private Integer questionTypeId;
		private Integer questionCategoryId;
		private Integer questionSubCategoryId;
		private Integer typeAnswer;

		public Integer getRecordId(){return recordId;}
		public Integer getServiceId(){return serviceId;}
		public Integer getServiceVariationId(){return serviceVariationId;}
		public Integer getQuestionTypeId()	{return questionTypeId;}
		public Integer getQuestionCategoryId(){return questionCategoryId;}
		public Integer getQuestionSubCategoryId(){return questionSubCategoryId;}
		public Integer getTypeAnswer(){return typeAnswer;}
		
		public void setRecordId(Integer recordId){	this.recordId = recordId;}
		public void setServiceId(Integer serviceId){	this.serviceId = serviceId;}
		public void setServiceVariationId(Integer serviceVariationId){this.serviceVariationId = serviceVariationId;}
		public void setQuestionTypeId(Integer questionTypeId){this.questionTypeId = questionTypeId;}
		public void setQuestionCategoryId(Integer questionCategoryId){	this.questionCategoryId = questionCategoryId;}
		public void setQuestionSubCategoryId(Integer questionSubCategoryId){this.questionSubCategoryId = questionSubCategoryId;}
		public void setTypeAnswer(Integer typeAnswer){	this.typeAnswer = typeAnswer;}
	}

	// class for "Waiting Timeline" records with the specified data
	public static class RecordWT extends Record
	{
		private Date date;
		private Integer time;

		public Integer getTime()	{return time;}
		public Date getDate()	{return date;	}
		public void setTime(Integer time)	{this.time = time;}
		public void setDate(Date date){this.date = date;}
	}

	// class for "Query" records
	public static class RecordQuery extends Record
	{
 // array contains linked "Waiting Timeline" records which query need to process. 
		// contains RecordWT object IDs
		Collection<Integer> arrIdRecordWTLinked = new ArrayList<Integer>();
  // array contains each RecordWT object founded after find function. 
		// contains RecordWT object IDs
		Collection<Integer> arrIdRecordWTFound = new ArrayList<Integer>();
		Date dateFrom;
		Date dateTo;

		public Date getDateFrom(){	return dateFrom;}
		public Date getDateTo()	{return dateTo;	}
		public Collection<Integer> getArrIdRecordWT()	{return arrIdRecordWTLinked;	}
		
		public void setDateFrom(Date date){this.dateFrom = date;}
		public void setDateTo(Date date){this.dateTo = date;}
		public void setArrIdRecordWT(Collection<Integer> arr){this.arrIdRecordWTLinked = arr;}

		public Collection<Integer> find(ArrayList<RecordWT> arrRecordWT)
		{
			for (int i = 0; i < arrRecordWT.size(); ++i)
			{
				if (arrIdRecordWTLinked.contains(arrRecordWT.get(i).getRecordId()))
				{
					// looking all object parameters with NOT NULL value and create array of filters
					FactoryFilter filter = new Filter().new FactoryFilter();

					ArrayList<IFilter> filters = new ArrayList<IFilter>();

					if (getServiceId() != null)
						filters.add(filter.getFilter(FilterTypes.SERVICE_ID));
					if (getServiceVariationId() != null)
						filters.add(filter.getFilter(FilterTypes.SERVICE_VARIATION_ID));
					if (getQuestionTypeId() != null)
						filters.add(filter.getFilter(FilterTypes.QUESTION_TYPE_ID));
					if (getQuestionCategoryId() != null)
						filters.add(filter.getFilter(FilterTypes.QUESTION_CATEGORY_ID));
					if (getQuestionSubCategoryId() != null)
						filters.add(filter.getFilter(FilterTypes.QUESTION_SUBCATEGORY_ID));
					if (getDateFrom() != null)
						filters.add(filter.getFilter(FilterTypes.DATE));

					boolean is_found = true;
					for (int i_filter = 0; i_filter < filters.size(); ++i_filter)
						if (!filters.get(i_filter).filter(arrRecordWT.get(i), this))
							is_found = false;

					if (is_found)
						arrIdRecordWTFound.add(arrRecordWT.get(i).getRecordId());

				}
			}
			return arrIdRecordWTFound;
		}

		public Integer calculateTime(ArrayList<RecordWT> arrRecordWT)
		{
			Integer totalTime = 0;
			int amountRecordWT = 0;

			for (int i = 0; i < arrRecordWT.size(); ++i)
			{
				if (arrIdRecordWTFound.contains(arrRecordWT.get(i).getRecordId()))
				{
					++amountRecordWT;
					totalTime += arrRecordWT.get(i).getTime();
				}
			}
			return amountRecordWT == 0 ? -1 : (totalTime / amountRecordWT);
		}
	}

	private String getRawData(String filename)
	{
		return 
		 "7\n" 
		  + "C 1.1 8.15.1 P 15.10.2012 83\n"
				+ "C 1 10.1 P 01.12.2012 65\n"
				+ "C 1.1 5.5.1 P 01.11.2012 117\n"
				+ "D 1.1 8 P 01.01.2012-01.12.2012\n" 
				+ "C 3 10.2 N 02.10.2012 100\n"
				+ "D 1 * P 8.10.2012-20.11.2012\n" 
				+ "D 3 10 P 01.12.2012";
	}

	public static void main(String[] args)
	{
		try
		{
			/*creating two array of objects RecordWT and RecordQuery.
			RecordQuery object contain array RecordWT IDs need for find information.
			when we need to create new query, we create a new object of RecordQuery, 
			show him which records of Waiting Timeline he need to use and call function find().
			after that object have are RecordWT IDs and we can call function calculateTime()*/
			
			Analytics analytics = new Analytics();
			BuilderRecord builderRecord = new BuilderRecord();

			ArrayList<RecordWT> arrRecordWT;
			ArrayList<RecordQuery> arrRecordQuery;

			String rawData = analytics.getRawData("data.txt");

			arrRecordWT = builderRecord.new BuilderRecordWT().parse(rawData);
			arrRecordQuery = builderRecord.new BuilderRecordQuery().parse(rawData);

			for (int i_query = 0; i_query < arrRecordQuery.size(); ++i_query)
			{
				arrRecordQuery.get(i_query).find(arrRecordWT);
				int result = arrRecordQuery.get(i_query).calculateTime(arrRecordWT);
				System.out.println(result == -1 ? "-" : result);
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
