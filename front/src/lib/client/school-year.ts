import { getAccessToken } from '@/lib/client/access_token';

export const setCurrentSchoolYear = async (id: string) => {
  const response = await fetch(`${process.env.API_URL}/school-year/current/${id}`, {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${await getAccessToken()}`,
    },
  });

  if (!response.ok)
    throw response.statusText;
};
