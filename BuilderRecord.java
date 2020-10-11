package main;

import java.text.SimpleDateFormat;
import java.util.*;

import main.Analytics.*;

public class BuilderRecord
{

	public class Parser
	{
		public ArrayList<String> parsePlainText(String rawData) throws Exception
		{
			{
				ArrayList<String> ret = new ArrayList<String>();

				String[] lines = rawData.split("\\n");
				int amount_line = 0;

				try
				{
					amount_line = Integer.parseInt(lines[0]);
				} catch (Exception ex){throw new Exception("wrong number of lines");}

				for (int i_line = 1; i_line <= amount_line; ++i_line)
					ret.add(lines[i_line].trim());

				return ret;
			}
		}
	}

	// parsing lines data and create array of RecordWT with specified data
	public class BuilderRecordWT extends Parser
	{
		public ArrayList<RecordWT> parse(String rawData) throws Exception
		{
			{
				ArrayList<RecordWT> arrRecordWT = new ArrayList<RecordWT>();
				ArrayList<String> lines = super.parsePlainText(rawData);

				if (lines != null)
				{
					for (int i_line = 0; i_line < lines.size(); ++i_line)
					{
						if (lines.get(i_line).charAt(0) == 'C')
						{
							RecordWT recWT = new RecordWT();
							recWT.setRecordId(i_line);

							String[] values = lines.get(i_line).split(" +");
							String[] service = values[1].split("\\.");
							if (!service[0].equals("*"))
							{
								recWT.setServiceId(Integer.valueOf(service[0]));
								if (service.length > 1)
									recWT.setServiceVariationId(Integer.valueOf(service[1]));
							}

							String[] question = values[2].split("\\.");
							if (!question[0].equals("*"))
							{
								recWT.setQuestionTypeId(Integer.valueOf(question[0]));
								if (question.length > 1)
									recWT.setQuestionCategoryId(Integer.valueOf(question[1]));
								if (question.length > 2)
									recWT.setQuestionSubCategoryId(Integer.valueOf(question[2]));
							}

							if (values[3].equals("P"))
								recWT.setTypeAnswer(0);
							else if (values[3].equals("N"))
								recWT.setTypeAnswer(1);
							else
								throw new Exception("wrong answer type");

							recWT.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(values[4]));

							recWT.setTime(Integer.valueOf(values[5]));

							arrRecordWT.add(recWT);
						}
					}
				}
				return arrRecordWT;
			}
		}
	}

	// parsing lines data and create array of RecordQuery with specified data and the necessary connections with RecordWT
	public class BuilderRecordQuery extends Parser
	{
		public ArrayList<RecordQuery> parse(String rawData) throws Exception
		{
			{
				ArrayList<RecordQuery> arrRecordQuery = new ArrayList<RecordQuery>();
				Collection<Integer> RecordWTListIdBefore = new ArrayList<Integer>();
				ArrayList<String> lines = super.parsePlainText(rawData);

				if (lines != null)
				{
					for (int i_line = 0; i_line < lines.size(); ++i_line)
					{
						if (lines.get(i_line).charAt(0) == 'C')
							RecordWTListIdBefore.add(i_line);
						if (lines.get(i_line).charAt(0) == 'D')
						{
							RecordQuery recQuery = new RecordQuery();
							recQuery.setRecordId(i_line);

							String[] values = lines.get(i_line).split(" +");
							String[] service = values[1].split("\\.");
							if (!service[0].equals("*"))
							{
								recQuery.setServiceId(Integer.valueOf(service[0]));
								if (service.length > 1)
									recQuery.setServiceVariationId(Integer.valueOf(service[1]));
							}

							String[] question = values[2].split("\\.");
							if (!question[0].equals("*"))
							{
								recQuery.setQuestionTypeId(Integer.valueOf(question[0]));
								if (question.length > 1)
									recQuery.setQuestionCategoryId(Integer.valueOf(question[1]));
								if (question.length > 2)
									recQuery.setQuestionSubCategoryId(Integer.valueOf(question[2]));
							}

							if (values[3].equals("P"))
								recQuery.setTypeAnswer(0);
							else if (values[3].equals("N"))
								recQuery.setTypeAnswer(1);
							else
								throw new Exception("wrong answer type");

							if (values.length > 4)
							{
								String[] date = values[4].split("-");
								recQuery.setDateFrom(new SimpleDateFormat("dd.MM.yyyy").parse(date[0]));
								if (date.length > 1)
									recQuery.setDateTo(new SimpleDateFormat("dd.MM.yyyy").parse(date[1]));
							}

							recQuery.setArrIdRecordWT(RecordWTListIdBefore);
							arrRecordQuery.add(recQuery);
						}
					}
				}
				return arrRecordQuery;
			}
		}
	}
}
