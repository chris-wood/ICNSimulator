function [ ] = distance_plot_from_file( )

fileName = sprintf('tree_2_5');
M1 = csvread(sprintf('%s.ccn.message.ContentObject.csv', fileName));
M2 = csvread(sprintf('%s.ccn.message.Interest.csv', fileName));
M3 = csvread(sprintf('%s.ccn.message.VirtualInterest.csv', fileName));

domain = M1(:,1);
contentValues = M1(:,2);
interestCounts = M2(:,2);
vinterestCounts = M3(:,2);

plot(domain, contentValues, 'DisplayName', 'Content Objects'); 
hold on; 
plot(domain, interestCounts, 'DisplayName', 'Interests'); 
plot(domain, vinterestCounts, 'DisplayName', 'Virtual Interests'); 
hold off;

legend('Content Objects', 'Interests', 'Virtual Interests');

print(fileName,'-depsc');

end

