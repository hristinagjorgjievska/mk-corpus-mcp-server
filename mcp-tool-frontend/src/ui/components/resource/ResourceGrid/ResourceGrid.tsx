import { Box } from '@mui/material';
import type { ResourceResponse } from '../../../../api/types/resource.ts';
import ResourceCard from '../ResourceCard/ResourceCard.tsx';

interface ResourceGridProps {
    resources: ResourceResponse[];
    onAnalyze: (id: number) => void;
    onDelete: (id: number) => void;
}

const ResourceGrid = ({ resources, onAnalyze, onDelete }: ResourceGridProps) => {
    return (
        <Box>
            {resources.map((resource, index) => (
                <ResourceCard
                    key={resource.id}
                    resource={resource}
                    index={index}
                    onAnalyze={onAnalyze}
                    onDelete={onDelete}
                />
            ))}
        </Box>
    );
};

export default ResourceGrid;
