import { getAccessToken } from '@/lib/server/access_token';
import { CustomField } from '@/types/custom-field';

export const getCustomField = async (): Promise<CustomField[]> => {
  const response = await fetch(`${process.env.API_URL}/custom-field`, {
    headers: {
      Authorization: `Bearer ${await getAccessToken()}`,
    },
  });

  if (!response.ok) throw response.statusText;

  return await response.json();
};

export const createCustomField = async (
  customField: CustomField
): Promise<CustomField> => {
  const response = await fetch(`${process.env.API_URL}/custom-field`, {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${await getAccessToken()}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(customField),
  });

  if (!response.ok) throw response.statusText;

  return await response.json();
};
