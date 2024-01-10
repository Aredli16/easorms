import { SchoolYear } from '@/types/school-year';
import { getAccessToken } from '@/lib/server/access_token';

export const getSchoolYear = async (): Promise<SchoolYear[]> => {
  const response = await fetch(`${process.env.API_URL}/school-year`, {
    headers: {
      Authorization: `Bearer ${await getAccessToken()}`,
    },
  });

  if (!response.ok) throw response.statusText;

  return await response.json();
};

export const createSchoolYear = async (
  schoolYear: SchoolYear
): Promise<SchoolYear> => {
  const response = await fetch(`${process.env.API_URL}/school-year`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${await getAccessToken()}`,
    },
    body: JSON.stringify(schoolYear),
  });

  if (!response.ok) throw response.statusText;

  return await response.json();
};
