import { SchoolYear } from '@/types/school_year';
import { getAccessToken } from '@/lib/server/access_token';

export const getSchoolYear = async (): Promise<SchoolYear[]> => {
  const response = await fetch(`${process.env.API_URL}/school-year`, {
    headers: {
      Authorization: `Bearer ${await getAccessToken()}`,
    },
  });

  if (!response.ok)
    throw response.statusText;

  return await response.json();
};
